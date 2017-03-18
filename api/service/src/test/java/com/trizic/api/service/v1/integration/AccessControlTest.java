package com.trizic.api.service.v1.integration;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.testng.annotations.Test;
import com.trizic.api.service.v1.Registration;

@Test(groups="functional")
public class AccessControlTest extends IntegrationTest {

    public void notAuthorizedMissingHeader() {
        Registration registration = registerAdvisor();
        Response response = getWebTarget()
                .path( registration.getAdvisorId() )
                .path( "model" )
                .request()
                .accept( MediaType.APPLICATION_JSON )
                .get();

        assert response.getStatus() == Status.UNAUTHORIZED.getStatusCode()
            : "Unexpected status code: " + response.getStatus() + ", expected " + Status.UNAUTHORIZED.getStatusCode();
    }

    public void notAuthorizedNotBearer() {
        Registration registration = registerAdvisor();
        Response response = getWebTarget()
                .path( registration.getAdvisorId() )
                .path( "model" )
                .request()
                .header( "Authorization", "ForeBearer " + registration.getApiKey() )
                .accept( MediaType.APPLICATION_JSON )
                .get();

        assert response.getStatus() == Status.UNAUTHORIZED.getStatusCode()
            : "Unexpected status code: " + response.getStatus() + ", expected " + Status.UNAUTHORIZED.getStatusCode();
    }

    public void notAuthorizedMissingToken() {
        Registration registration = registerAdvisor();
        Response response = getWebTarget()
                .path( registration.getAdvisorId() )
                .path( "model" )
                .request()
                .header( "Authorization", "Bearer " )
                .accept( MediaType.APPLICATION_JSON )
                .get();

        assert response.getStatus() == Status.FORBIDDEN.getStatusCode()
            : "Unexpected status code: " + response.getStatus() + ", expected " + Status.FORBIDDEN.getStatusCode();
    }

    public void notAuthorizedInvalidAdvisor() {
        Registration registration = registerAdvisor();
        Response response = getWebTarget()
                .path( "1234" )
                .path( "model" )
                .request()
                .header( "Authorization", "Bearer " + registration.getApiKey() )
                .accept( MediaType.APPLICATION_JSON )
                .get();

        assert response.getStatus() == Status.FORBIDDEN.getStatusCode()
            : "Unexpected status code: " + response.getStatus() + ", expected " + Status.FORBIDDEN.getStatusCode();
    }
}
