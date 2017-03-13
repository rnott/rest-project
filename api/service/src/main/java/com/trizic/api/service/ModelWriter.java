package com.trizic.api.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.trizic.api.model.ModelRequest;
import com.trizic.api.model.ModelResponse;
import com.trizic.api.model.Util;


public class ModelWriter implements Persistence {

    static final Path ROOT = Advisors.ROOT;

    private final String advisor;
    private final ObjectMapper mapper;

    private ModelRequest model;

    public ModelWriter( String advisor ) {
        this.advisor = advisor;
        if ( advisor == null || advisor.length() == 0 ) {
            throw new IllegalArgumentException( "Advisor must be assigned a non-empty value" );
        }
        if ( ! ROOT.resolve( advisor ).toFile().exists() ) {
            throw new IllegalArgumentException( "Advisor must be registered: " + advisor );
        }

        mapper = new ObjectMapper();
        mapper.configure( Feature.AUTO_CLOSE_SOURCE, true )
            .configure( MapperFeature.AUTO_DETECT_FIELDS, true )
            .configure( SerializationFeature.INDENT_OUTPUT, true );
    }

    public ModelWriter withModel( ModelRequest model ) {
        this.model = model;
        return this;
    }

    public void write() {
        if ( this.model == null || (! Util.validateHoldingAndAssetAllocation( this.model )) ) {
            throw new IllegalArgumentException( "Submitted model is invalid" );
        }

        ModelResponse target;
        if ( containsModel( this.model.getName() ) ) {
            try {
                target = mapper.readValue( asFile( this.model.getName() ), ModelResponse.class );
            } catch ( IOException e ) {
                throw new IllegalStateException( "Failed to read existing model: " + advisor + "/" + this.model.getName(), e );
            }
        } else {
            target = new ModelResponse()
                .withAdvisorId( advisor )
                .withGuid( UUID.randomUUID().toString() )
                .withCreatedOn( DATE_TIME.format( new Date() ) );
        }

        // transform and write
        target = asModelResponse( this.model, target );
        try {
            mapper.writeValue( asFile( this.model.getName() ), target );
        } catch ( IOException e ) {
            throw new IllegalStateException( "Failed to write model: " + advisor + "/" + this.model.getName(), e );
        }
    }

    private SimpleDateFormat DATE_TIME = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssZ" );

    private ModelResponse asModelResponse( ModelRequest source, ModelResponse target ) {
        return target
            .withName( source.getName() )
            .withDescription( source.getDescription() )
            .withModelType( ModelResponse.ModelType.valueOf( source.getModelType().name() ) )
            .withRebalanceFrequency( ModelResponse.RebalanceFrequency.valueOf( source.getRebalanceFrequency().name() ) )
            .withCashHoldingPercentage( source.getCashHoldingPercentage() )
            .withDriftPercentage( source.getDriftPercentage() )
            .withAssetAllocations( source.getAssetAllocations() );
    }

    private File asFile( String name ) {
        return ROOT.resolve( advisor ).resolve( name + ".json" ).toFile();
    }

    private boolean containsModel( String name ) {
        return asFile( name ).exists();
    }
}
