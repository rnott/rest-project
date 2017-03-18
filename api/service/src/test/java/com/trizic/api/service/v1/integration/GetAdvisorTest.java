package com.trizic.api.service.v1.integration;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.trizic.api.service.v1.Model;
import com.trizic.api.service.v1.ModelPage;
import com.trizic.api.service.v1.ModelResponse;
import com.trizic.api.service.v1.Registration;
import com.trizic.api.service.v1.RequestError;


@Test(groups="functional")
public class GetAdvisorTest extends IntegrationTest {

    private Registration registration;

    private String getModelName( int counter ) {
        return "test-" + String.format( "%03d", counter );
    }

    @BeforeClass
    public void load() throws IOException {
        registration = registerAdvisor();

        // create some data
        for ( int i = 0; i < 100; i++ ) {
            Model model = loadResource( "/example-model-1.json", Model.class );
            model.withName( getModelName( i ) );
            Response response = getWebTarget()
                .path( registration.getAdvisorId() )
                .path( "model" )
                .request()
                .header( "Authorization", "Bearer " + registration.getApiKey() )
                .accept( MediaType.APPLICATION_JSON )
                .put( Entity.entity( model, MediaType.APPLICATION_JSON ) );
            assert response.getStatus() == Status.OK.getStatusCode()
                : "Failed to create model: " + response.getStatus() + " [" + response.readEntity( String.class ) + " ]";
        }
    }

    public void getPageWithDefaults() throws IOException {
        Response response = getWebTarget()
                .path( registration.getAdvisorId() )
                .path( "model" )
                .request()
                .header( "Authorization", "Bearer " + registration.getApiKey() )
                .accept( MediaType.APPLICATION_JSON )
                .get();
        assert response.getStatus() == Status.OK.getStatusCode()
            : "Unexpected status code: " + response.getStatus() + ", expected " + Status.OK.getStatusCode();
        ModelPage page = response.readEntity( ModelPage.class );
        assert page != null : "Response body was null";
        assert page.getTotalNumberOfElements() == 100
            : "Unexpected total elements: " + page.getTotalNumberOfElements();
        assert page.getPageNumber() == 1
            : "Unexpected page number: " + page.getPageNumber();
        assert page.getPageSize() == 20
            : "Unexpected page size: " + page.getPageSize();
        assert page.getNumberOfPages() == 5
            : "Unexpected number of pages: " + page.getNumberOfPages();

        validatePageElements( page, 20 );
    }

    public void getPageWithPageNumber() throws IOException {
        Response response = getWebTarget()
                .path( registration.getAdvisorId() )
                .path( "model" )
                .queryParam( "pageNumber", 2 )
                .request()
                .header( "Authorization", "Bearer " + registration.getApiKey() )
                .accept( MediaType.APPLICATION_JSON )
                .get();
        assert response.getStatus() == Status.OK.getStatusCode()
            : "Unexpected status code: " + response.getStatus() + ", expected " + Status.OK.getStatusCode();
        ModelPage page = response.readEntity( ModelPage.class );
        assert page != null : "Response body was null";
        assert page.getTotalNumberOfElements() == 100
            : "Unexpected total elements: " + page.getTotalNumberOfElements();
        assert page.getPageNumber() == 2
            : "Unexpected page number: " + page.getPageNumber();
        assert page.getPageSize() == 20
            : "Unexpected page size: " + page.getPageSize();
        assert page.getNumberOfPages() == 5
            : "Unexpected number of pages: " + page.getNumberOfPages();

        validatePageElements( page, 20 );
    }

    public void getPageWithPageSize() throws IOException {
        Response response = getWebTarget()
                .path( registration.getAdvisorId() )
                .path( "model" )
                .queryParam( "pageSize", 30 )
                .request()
                .header( "Authorization", "Bearer " + registration.getApiKey() )
                .accept( MediaType.APPLICATION_JSON )
                .get();
        assert response.getStatus() == Status.OK.getStatusCode()
            : "Unexpected status code: " + response.getStatus() + ", expected " + Status.OK.getStatusCode();
        ModelPage page = response.readEntity( ModelPage.class );
        assert page != null : "Response body was null";
        assert page.getTotalNumberOfElements() == 100
            : "Unexpected total elements: " + page.getTotalNumberOfElements();
        assert page.getPageNumber() == 1
            : "Unexpected page number: " + page.getPageNumber();
        assert page.getPageSize() == 30
            : "Unexpected page size: " + page.getPageSize();
        assert page.getNumberOfPages() == 4
            : "Unexpected number of pages: " + page.getNumberOfPages();

        validatePageElements( page, 30 );
    }

    public void getPageWithPagePartial() throws IOException {
        Response response = getWebTarget()
                .path( registration.getAdvisorId() )
                .path( "model" )
                .queryParam( "pageSize", 30 )
                .queryParam( "pageNumber", 4 )
                .request()
                .header( "Authorization", "Bearer " + registration.getApiKey() )
                .accept( MediaType.APPLICATION_JSON )
                .get();
        assert response.getStatus() == Status.OK.getStatusCode()
            : "Unexpected status code: " + response.getStatus() + ", expected " + Status.OK.getStatusCode();
        ModelPage page = response.readEntity( ModelPage.class );
        assert page != null : "Response body was null";
        assert page.getTotalNumberOfElements() == 100
            : "Unexpected total elements: " + page.getTotalNumberOfElements();
        assert page.getPageNumber() == 4
            : "Unexpected page number: " + page.getPageNumber();
        assert page.getPageSize() == 30
            : "Unexpected page size: " + page.getPageSize();
        assert page.getNumberOfPages() == 4
            : "Unexpected number of pages: " + page.getNumberOfPages();

        validatePageElements( page, 10 );
    }

    public void invalidPageSize() {
        Response response = getWebTarget()
                .path( registration.getAdvisorId() )
                .path( "model" )
                .queryParam( "pageSize", 0 )
                .request()
                .header( "Authorization", "Bearer " + registration.getApiKey() )
                .accept( MediaType.APPLICATION_JSON )
                .get();
        assert response.getStatus() == Status.BAD_REQUEST.getStatusCode()
            : "Unexpected status code: " + response.getStatus() + ", expected " + Status.BAD_REQUEST.getStatusCode();
        RequestError error = response.readEntity( RequestError.class );
        assert error.getErrorCode() != null : "Error code is null";
        assert error.getErrorCode().equals( "request.invalid" )
            : "Unexpected error code: " + error.getErrorCode() + ", expected request.invalid";
    }

    public void invalidPageNumber() {
        Response response = getWebTarget()
                .path( registration.getAdvisorId() )
                .path( "model" )
                .queryParam( "pageNumber", 0 )
                .request()
                .header( "Authorization", "Bearer " + registration.getApiKey() )
                .accept( MediaType.APPLICATION_JSON )
                .get();
        assert response.getStatus() == Status.BAD_REQUEST.getStatusCode()
            : "Unexpected status code: " + response.getStatus() + ", expected " + Status.BAD_REQUEST.getStatusCode();
        RequestError error = response.readEntity( RequestError.class );
        assert error.getErrorCode() != null : "Error code is null";
        assert error.getErrorCode().equals( "request.invalid" )
            : "Unexpected error code: " + error.getErrorCode() + ", expected request.invalid";
    }

    private void validatePageElements( ModelPage page, int elementCount ) throws IOException {
        Model source = loadResource( "/example-model-1.json", Model.class );

        // test fetch of correct N entries
        List<ModelResponse> elements = page.getPage();
        assert elements != null : "Page elements is null";
        assert elements.size() == elementCount
            : "Unexpected number of elements: " + elements.size() + ", expected " + elementCount;

        int n = page.getPageSize() * (page.getPageNumber() - 1);
        for ( ModelResponse model : elements ) {
            assert registration.getAdvisorId().equals( model.getAdvisorId() )
                : "Unexpected advisorId: " + model.getAdvisorId() + ", expected " + registration.getAdvisorId();
            assert model.getGuid() != null : "GUID is null";
            assert model.getCreatedOn() != null : "Created on is null";
            SimpleDateFormat dateTime = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssZ" );
            try {
                dateTime.parse( model.getCreatedOn() );
            } catch ( ParseException e ) {
                throw new RuntimeException( "Invalid DATE_TIME format: " + model.getCreatedOn() );
            }
            String expect = getModelName( n );
            assert expect.equals( model.getName() ) :
                "Unexpected model name: " + model.getName() + ", expected: " + expect;
            assert source.getDescription().equals( model.getDescription() )
                : "Unexpected description: " + model.getDescription() + ", expected " + source.getDescription();
            assert source.getModelType() == model.getModelType()
                : "Unexpected model type: " + model.getModelType() + ", expected " + source.getModelType();
            assert source.getRebalanceFrequency() == model.getRebalanceFrequency()
                : "Unexpected rebalancing frequency: " + model.getRebalanceFrequency()
                + ", expected " + source.getRebalanceFrequency();
            assert source.getCashHoldingPercentage().equals( model.getCashHoldingPercentage() )
                : "Unexpected cash holding pct: " + model.getCashHoldingPercentage()
                + ", expected " + source.getCashHoldingPercentage();
            assert source.getDriftPercentage().equals( model.getDriftPercentage() )
                : "Unexpected drift pct: " + model.getDriftPercentage() + ", expected " + source.getDriftPercentage();
            assert model.getAssetAllocations() != null : "Asset allocations is null";
            assert source.getAssetAllocations().size() == model.getAssetAllocations().size()
                : "Unexpected number of asset allocations: " + model.getAssetAllocations().size()
                + ", expected " + source.getAssetAllocations().size();
            for ( int i = 0; i < source.getAssetAllocations().size(); i++ ) {
                String st = source.getAssetAllocations().get( i ).getSymbol();
                Double sp = source.getAssetAllocations().get( i ).getPercentage();
                String mt = model.getAssetAllocations().get( i ).getSymbol();
                Double mp = model.getAssetAllocations().get( i ).getPercentage();
                assert st.equals( mt ) : "Unexpected asset allocation symbol [" + i + "]: "
                    + mt + ", expected " + st;
                assert sp.equals( mp ) : "Unexpected asset allocation percentage [" + i + "]: "
                + mp + ", expected " + sp;
            }
            n++;
        }
    }
}
