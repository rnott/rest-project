package com.trizic.api.service.v1;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.fasterxml.jackson.databind.ObjectMapper;


public class ModelWriterTest {

    private Model generateModel( String advisor ) throws IOException {
        return new Model()
            .withName( "test" )
            .withDescription( "Test" )
            .withModelType( Model.ModelType.QUALIFIED )
            .withRebalanceFrequency( Model.RebalanceFrequency.ANNUAL )
            .withCashHoldingPercentage( 10 )
            .withDriftPercentage( 10 )
            .withAssetAllocations( generateAssetAllocation( 4, 10 ) );
    }

    private List<AssetAllocation> generateAssetAllocation( int count, int reserve ) {
        List<AssetAllocation> list = new ArrayList<AssetAllocation>( count );
        BigDecimal percentage = new BigDecimal( 100 - reserve ).divide( new BigDecimal( count ) );
        for ( int i = 0; i < count; i++ ) {
            list.add(
                new AssetAllocation()
                    .withSymbol( String.valueOf( i ) )
                    .withPercentage( percentage.doubleValue() )
                );
        }
        return list;
    }

    private void remove( File file ) {
        if ( file.isDirectory() ) {
            for ( File f : file.listFiles() ) {
                remove( f );
            }
        }
        file.delete();
    }

    private void cleanup() {
        for ( File f : Persistence.Advisors.getStorage().listFiles() ) {
            remove( f );
        }
    }

    private void createAdvisor( String advisor ) {
        new File( Persistence.Advisors.getStorage(), advisor ).mkdirs();
    }

    private File createFile( String advisor, String name ) {
        return new File( Persistence.Advisors.getStorage( advisor ), name );
    }

    private ModelResponse readFile( String advisor, String name ) throws IOException {
        return new ObjectMapper()
            .readValue(
                new File( Persistence.Advisors.getStorage( advisor ), name ),
                ModelResponse.class
            );
    }

    @DataProvider(name = "writeParams")
    public Object[][] writeParams() throws IOException {
        return new Object[][] {
            { "foo", generateModel( "foo" ) },
        };
    }

    @Test(dataProvider = "writeParams")
    public void write( String advisor, Model model ) throws IOException {
        cleanup();
        createAdvisor( advisor );

        new ModelWriter( advisor )
            .withModel( model )
            .write();

        File entry = createFile( advisor, model.getName() + ".json" );
        assert entry.exists() : "Expected model file to exist: " + entry.getAbsolutePath();
        assert entry.isFile() : "Persisted model is not a file";
        assert entry.length() > 0 : "Persisted model is zero-length";

        ModelResponse target = readFile( advisor, model.getName() + ".json" );
        assert target.getAdvisorId().equals( advisor ) :
            "Unexpected advisor: " + target.getAdvisorId() + ", expected: " + advisor;
        assert target.getName().equals( model.getName() ) :
            "Unexpected name: " + target.getName() + ", expected: " + model.getName();
        assert target.getDescription().equals( model.getDescription() ) :
            "Unexpected description: " + target.getDescription() + ", expected: " + model.getDescription();
        assert target.getModelType().name().equals( model.getModelType().name() ) :
            "Unexpected type: " + target.getModelType() + ", expected: " + model.getModelType();
        assert target.getRebalanceFrequency().name().equals( model.getRebalanceFrequency().name() ) :
            "Unexpected rebalance frequency: " + target.getRebalanceFrequency() + ", expected: "
            + model.getRebalanceFrequency();
        assert target.getCashHoldingPercentage().equals( model.getCashHoldingPercentage() ) :
            "Unexpected cash holding percentage: " + target.getCashHoldingPercentage()
            + ", expected: " + model.getCashHoldingPercentage();
        assert target.getDriftPercentage().equals( model.getDriftPercentage() ) :
            "Unexpected drift percentage: " + target.getDriftPercentage() + ", expected: " + model.getDriftPercentage();
        assert target.getAssetAllocations().size() == model.getAssetAllocations().size() :
            "Unexpected number of asset allocations: " + target.getAssetAllocations().size()
            + ", expected: " + model.getAssetAllocations().size();
        assert target.getGuid() != null : "GUID was not assigned";
        assert target.getCreatedOn() != null : "Creation date was not assigned";
        for ( int i = 0; i < target.getAssetAllocations().size(); i++ ) {
            String expect = model.getAssetAllocations().get( i ).getSymbol();
            String actual = target.getAssetAllocations().get( i ).getSymbol();
            assert actual.equals( expect ) : "Unexpected asset[" + i + "] symbol: " + actual + ", expected: " + expect;
        }
        for ( int i = 0; i < target.getAssetAllocations().size(); i++ ) {
            Double expect = model.getAssetAllocations().get( i ).getPercentage();
            Double actual = target.getAssetAllocations().get( i ).getPercentage();
            assert actual.equals( expect ) : "Unexpected asset[" + i + "] percentage: " + actual + ", expected: " + expect;
        }
    }

    @Test(expectedExceptions = NoSuchAdvisorException.class)
    public void writeNullAdvisor() {
        cleanup();
        new ModelWriter( null );
    }

    // due to the use of jwt we don't have to rely on an existing directory
    // as proof of registration
    @Test(enabled = false, expectedExceptions = NoSuchAdvisorException.class)
    public void writeNoAdvisor() {
        cleanup();
        new ModelWriter( "foo" );
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void writeNoModel() {
        cleanup();
        createAdvisor( "foo" );
        new ModelWriter( "foo" ).write();
    }

    // model validation is performed by Bean Validation rather than internally
    @Test(enabled = false, expectedExceptions = IllegalArgumentException.class)
    public void writeInvalidModel() {
        cleanup();
        createAdvisor( "foo" );

        // allocation mismatch
        Model req = new Model()
            .withAssetAllocations( this.generateAssetAllocation( 1, 10 ) )
            .withCashHoldingPercentage( 20 )
            .withDescription( "TEST" )
            .withDriftPercentage( 5 )
            .withModelType( Model.ModelType.QUALIFIED )
            .withName( "test" )
            .withRebalanceFrequency( Model.RebalanceFrequency.ANNUAL );

        new ModelWriter( "foo" ).withModel( req ).write();
    }
}
