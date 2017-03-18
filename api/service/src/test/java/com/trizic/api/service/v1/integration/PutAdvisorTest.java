package com.trizic.api.service.v1.integration;

import java.io.IOException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.testng.annotations.Test;
import com.trizic.api.service.v1.Model;
import com.trizic.api.service.v1.Registration;
import com.trizic.api.service.v1.RequestError;


@Test(groups="functional")
public class PutAdvisorTest extends IntegrationTest {

    public void noModel() {
        Registration registration = registerAdvisor();
        Response response = getWebTarget()
            .path( registration.getAdvisorId() )
            .path( "model" )
            .request()
            .header( "Authorization", "Bearer " + registration.getApiKey() )
            .accept( MediaType.APPLICATION_JSON )
            .put( null );
        assert response.getStatus() == Status.BAD_REQUEST.getStatusCode()
            : "Unexpected response code: " + response.getStatus() + ", expected: " + Status.BAD_REQUEST.getStatusCode();
        RequestError error = response.readEntity( RequestError.class );
        assert error != null : "Error payload is null";
        assert error.getErrorCode() != null : "Error code is null";
        assert error.getErrorCode().equals( "request.invalid" )
            : "Unexpected error code: " + error.getErrorCode() + ", expected: request.invalid";
    }

    public void emptyModel() {
        Registration registration = registerAdvisor();
        Response response = getWebTarget()
            .path( registration.getAdvisorId() )
            .path( "model" )
            .request()
            .header( "Authorization", "Bearer " + registration.getApiKey() )
            .accept( MediaType.APPLICATION_JSON )
            .put( Entity.entity( new Model(), MediaType.APPLICATION_JSON ) );
        assert response.getStatus() == Status.BAD_REQUEST.getStatusCode()
            : "Unexpected response code: " + response.getStatus() + ", expected: " + Status.BAD_REQUEST.getStatusCode();
        RequestError error = response.readEntity( RequestError.class );
        assert error != null : "Error payload is null";
        assert error.getErrorCode() != null : "Error code is null";
        // multiple errors are generated during validation
        // but only one is arbitrarily returned as the spec only
        // accounts for a single error code
    }

    // this is no longer possible as the access control filter
    // will deny any request where the advisor doesn't match
    // the bearer token
    @Test(enabled = false)
    public void advisorNotFound() throws IOException {
        Registration registration = registerAdvisor();
        Response response = getWebTarget()
            .path( "11234" )
            .path( "model" )
            .request()
            .header( "Authorization", "Bearer " + registration.getApiKey() )
            .accept( MediaType.APPLICATION_JSON )
            .put( Entity.entity( this.loadResource( "/example-model-1.json", Model.class ), MediaType.APPLICATION_JSON ) );
        assert response.getStatus() == Status.NOT_FOUND.getStatusCode()
            : "Unexpected response code: " + response.getStatus() + ", expected: " + Status.NOT_FOUND.getStatusCode();
        RequestError error = response.readEntity( RequestError.class );
        assert error != null : "Error payload is null";
        assert error.getErrorCode() != null : "Error code is null";
        assert error.getErrorCode().equals( "advisor.not.found" )
            : "Unexpected error code: " + error.getErrorCode() + ", expected: advisor.not.found";
    }

    public void noModelName() throws IOException {
        missingProperty( "/invalid-model-name.json", "name.invalid" );
    }

    public void noModelDescription() throws IOException {
        missingProperty( "/invalid-model-description.json", "description.invalid" );
    }

    public void noModelCashHoldingPercentage() throws IOException {
        missingProperty( "/invalid-model-cash.json", "cashHoldingPercentage.invalid", "allocation.percentage.total.invalid" );
    }

    public void modelCashHoldingPercentageFloor() throws IOException {
        missingProperty( "/invalid-model-cash-min.json", "cashHoldingPercentage.invalid", "allocation.percentage.total.invalid" );
    }

    public void modelCashHoldingPercentageCeiling() throws IOException {
        missingProperty( "/invalid-model-cash-max.json", "cashHoldingPercentage.invalid", "allocation.percentage.total.invalid" );
    }

    public void noModelDriftPercentage() throws IOException {
        missingProperty( "/invalid-model-drift.json", "driftPercentage.invalid" );
    }

    public void modelDriftPercentageFloor() throws IOException {
        missingProperty( "/invalid-model-drift-min.json", "driftPercentage.invalid" );
    }

    public void modelCDriftPercentageCeiling() throws IOException {
        missingProperty( "/invalid-model-drift-max.json", "driftPercentage.invalid" );
    }

    public void noModelType() throws IOException {
        // jersey throws parse error (BAD REQUEST) for invalid enums
        missingProperty( "/invalid-model-type.json", "modelType.invalid", "request.invalid" );
    }

    public void noRebalanceFrequency() throws IOException {
        missingProperty( "/invalid-model-rebalance.json", "rebalanceFrequency.invalid" );
    }

    public void noAssetAllocations() throws IOException {
        missingProperty( "/invalid-model-assets.json", "assetAllocations.invalid", "allocation.percentage.total.invalid" );
    }

    public void zeroAssetAllocations() throws IOException {
        missingProperty( "/invalid-model-assets-zero.json", "allocation.percentage.total.invalid" );
    }

    public void noAssetAllocationSymbol() throws IOException {
        missingProperty( "/invalid-model-assets-symbol.json", "assetAllocations.symbol.invalid" );
    }

    public void noAssetAllocationPercentage() throws IOException {
        missingProperty( "/invalid-model-assets-pct.json", "assetAllocations.percentage.invalid" );
    }

    public void assetAllocationNot100Percent() throws IOException {
        missingProperty( "/invalid-model-assets-not100.json", "allocation.percentage.total.invalid" );
    }

    public void putModel() throws IOException {
        Registration registration = registerAdvisor();
        Response response = getWebTarget()
            .path( registration.getAdvisorId() )
            .path( "model" )
            .request()
            .header( "Authorization", "Bearer " + registration.getApiKey() )
            .accept( MediaType.APPLICATION_JSON )
            .put( Entity.entity( loadResource( "/example-model-1.json", Model.class ), MediaType.APPLICATION_JSON ) );
        assert response.getStatus() == Status.OK.getStatusCode()
            : "Unexpected response code: " + response.getStatus() + ", expected: " + Status.OK.getStatusCode();

    }

    private void missingProperty( String resource, String ... errorCodes ) throws IOException {
        Registration registration = registerAdvisor();
        Response response = getWebTarget()
            .path( registration.getAdvisorId() )
            .path( "model" )
            .request()
            .header( "Authorization", "Bearer " + registration.getApiKey() )
            .accept( MediaType.APPLICATION_JSON )
            .put( Entity.entity( loadResource( resource, Model.class ), MediaType.APPLICATION_JSON ) );
        assert response.getStatus() == Status.BAD_REQUEST.getStatusCode()
            : "Unexpected response code: " + response.getStatus()
            + ", expected: " + Status.BAD_REQUEST.getStatusCode()
            + " [" + response.readEntity( String.class ) + "]";
        RequestError error = response.readEntity( RequestError.class );
        assert error != null : "Error payload is null";
        assert error.getErrorCode() != null : "Error code is null";
        boolean match = false;
        for ( String code : errorCodes ) {
            match |= error.getErrorCode().equals( code );
        }
        assert match : "Unexpected error code: " + error.getErrorCode();
    }
}
