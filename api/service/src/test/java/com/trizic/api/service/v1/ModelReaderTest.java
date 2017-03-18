package com.trizic.api.service.v1;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.fasterxml.jackson.databind.ObjectMapper;


public class ModelReaderTest {

    private int generateModels( String advisor, int count ) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File advisorRoot = Persistence.Advisors.getStorage( advisor );
        advisorRoot.mkdirs();

        for ( int i = 0; i < count; i++ ) {
            String name = "test-" + i;
            ModelResponse resp = new ModelResponse();
            resp.withName( name )
                .withDescription( "Test" )
                .withModelType( ModelResponse.ModelType.QUALIFIED )
                .withRebalanceFrequency( ModelResponse.RebalanceFrequency.ANNUAL )
                .withCashHoldingPercentage( 10 )
                .withDriftPercentage( 10 )
                .withAssetAllocations( generateAssetAllocation( 4, 10 ) );
            resp.withAdvisorId( "1" )
                .withGuid( UUID.randomUUID().toString() )
                .withCreatedOn( "00:00:99T00:00:00Z" );
            mapper.writeValue( new File( advisorRoot, name + ".json" ), resp );
        }
        return count;
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

    @DataProvider(name = "fetchParams")
    public Object [][] fetchParams() throws IOException {
        return new Object [][] {
            { "foo", 1, 1, 0, 0 },
            { "bar", 1, 1, 0, 0 },
            { "foo", 1, 5, 5, 10 },  // page 1
            { "foo", 2, 5, 5, 10 },  // page 2
            { "foo", 1, 5, 5, 8 },   // page 1
            { "foo", 2, 5, 3, 8 },   // page 2
        };
    }

    @Test(dataProvider = "fetchParams")
    public void fetch( String advisor, int pageNumber, int pageSize, int resultElementCount, int totalElementCount ) throws IOException {
        // create the test data
        cleanup();
        generateModels( advisor, totalElementCount );

        ModelReader reader = new ModelReader( advisor )
            .withPageNumber( pageNumber )
            .withPageSize( pageSize );

        List<ModelResponse> elements = reader.fetch();

        assert reader.getAdvisor().equals( advisor ) :
            "Unexpected advisor: " + reader.getAdvisor() + ", expected: " + advisor;
        assert reader.getPageNumber() == pageNumber :
            "Unexpected page number: " + reader.getPageNumber() + ", expecpted: " + pageNumber;
        assert reader.getPageSize() == pageSize :
            "Unexpected page size: " + reader.getPageSize() + ", expected: " + pageSize;

        assert elements != null : "Result is null";
        assert elements.size() == resultElementCount :
            "Unexpected result count: " + elements.size() + ", expected: " + resultElementCount;
        assert reader.getTotalNumberOfElements() == totalElementCount :
            "Unexpected total number of elements: " + reader.getTotalNumberOfElements() + ", expected: " + totalElementCount;

        // test fetch of correct N entries
        int n = pageSize * (pageNumber - 1);
        for ( ModelResponse model : elements ) {
            String expect = "test-" + n;
            assert expect.equals( model.getName() ) :
                "Unexpected model name: " + model.getName() + ", expected: " + expect;
            n++;
        }
    }

    @Test(expectedExceptions = NoSuchAdvisorException.class)
    public void fetchNullAdvisorArgument() {
        cleanup();
        new ModelReader( null );
    }

    // due to the use of jwt we don't have to rely on an existing directory
    // as proof of registration
    @Test(enabled = false, expectedExceptions = NoSuchAdvisorException.class)
    public void fetchNoAdvisor() {
        cleanup();
        new ModelWriter( "foo" );
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void illegalPageSizeArgument() throws IOException {
        cleanup();
        generateModels( "foo", 0 );

        // expect to throw an exception
        new ModelReader( "foo" ).withPageSize( 0 );
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void illegalPageNumberArgument() throws IOException {
        cleanup();
        generateModels( "foo", 0 );

        // expect to throw an exception
        new ModelReader( "foo" ).withPageNumber( 0 );
    }
}
