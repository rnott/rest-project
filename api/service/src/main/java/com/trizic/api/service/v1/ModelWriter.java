package com.trizic.api.service.v1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


public class ModelWriter implements Persistence {

    private Logger logger = LoggerFactory.getLogger( ModelWriter.class );

    private final String advisor;
    private final ObjectMapper mapper;

    private Model model;

    public ModelWriter( String advisor ) {
        this.advisor = advisor;
        if ( advisor == null || advisor.length() == 0 ) {
            throw new NoSuchAdvisorException( advisor );
        }
        File f = Persistence.Advisors.getStorage( advisor );
        if ( ! f.exists() ) {
            // TODO: seems to be a timing issue when running unit tests
            // this shouldn't be necessary in general
            logger.debug( "Advisor storage not found: " + f.getAbsolutePath() );
            if ( f.mkdirs() ) {
                logger.warn( "Was able to create non-existent advisor storeage: " + f.getAbsolutePath() );
            } else {
                throw new NoSuchAdvisorException( advisor );
            }
        }

        mapper = new ObjectMapper();
        mapper.configure( Feature.AUTO_CLOSE_SOURCE, true )
            .configure( MapperFeature.AUTO_DETECT_FIELDS, true )
            .configure( SerializationFeature.INDENT_OUTPUT, true );
    }

    public ModelWriter withModel( Model model ) {
        this.model = model;
        return this;
    }

    public ModelResponse write() {
        if ( this.model == null ) {
            throw new IllegalArgumentException( "Submitted model is invalid" );
        }

        // read in existing as we need to guid, createdOn
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
        FileOutputStream out = null;
        try {
            File f = asFile( this.model.getName() );
            out = new FileOutputStream( f );
            mapper.writeValue( f, target );
            out.flush();
            logger.info( "Saved new advisor model: " + f.getAbsolutePath() );
        } catch ( IOException e ) {
            throw new IllegalStateException( "Failed to write model: " + advisor + "/" + this.model.getName(), e );
        } finally {
            try {
                out.close();
            } catch ( Throwable ignore ) {}
        }

        return target;
    }

    private SimpleDateFormat DATE_TIME = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssZ" );

    private ModelResponse asModelResponse( Model source, ModelResponse target ) {
        target
            .withName( source.getName() )
            .withDescription( source.getDescription() )
            .withModelType( ModelResponse.ModelType.valueOf( source.getModelType().name() ) )
            .withRebalanceFrequency( ModelResponse.RebalanceFrequency.valueOf( source.getRebalanceFrequency().name() ) )
            .withCashHoldingPercentage( source.getCashHoldingPercentage() )
            .withDriftPercentage( source.getDriftPercentage() )
            .withAssetAllocations( source.getAssetAllocations() );
        return target;
    }

    private File asFile( String name ) {
        return new File( Advisors.getStorage( advisor ), name + ".json" );
    }

    private boolean containsModel( String name ) {
        return asFile( name ).exists();
    }
}
