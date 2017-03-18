package com.trizic.api.service.v1.integration;

import java.io.IOException;
import java.net.URL;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.glassfish.jersey.client.ClientProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trizic.api.service.JettyServer;
import com.trizic.api.service.v1.Registration;


public class IntegrationTest {

    protected Logger logger = LoggerFactory.getLogger( this.getClass() );

    private Client client;
    private WebTarget webTarget;

    private JettyServer server;
    private ObjectMapper mapper = new ObjectMapper();

    protected <T> T loadResource( String path, Class<T> type ) throws IOException {
        URL resource = IntegrationTest.class.getResource( path );
        if ( resource == null ) {
            throw new IllegalStateException( "Resource not found: " + path );
        }
        return mapper.readValue( resource, type );
    }

    protected Registration registerAdvisor() {
        assert client != null : "JAX-RS Client not available";
        Response response = client.target( "http://localhost:8080/v1/registration/advisors" )
           .request()
           .accept( MediaType.APPLICATION_JSON )
           .post( null );
        assert response.getStatus() == Status.CREATED.getStatusCode()
            : "Failed to register advisor: " + response.getStatus();
        Registration registration = response.readEntity( Registration.class );
        assert registration != null : "Failed to register advisor";
        assert registration.getAdvisorId() != null : "No advisor id";
        assert registration.getApiKey() != null : "No api key";

        return registration;
    }

    protected WebTarget getWebTarget() {
        return webTarget;
    }

    @BeforeClass
    public void init() {
        client = ClientBuilder.newClient();
        assert client != null : "JAX-RS Client could not be created";
        // allow us to do 'unusual' things with the client
        client.property( ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION,  true );
        webTarget = client.target( "http://localhost:8080/v1/advisor" );
    }

    @AfterClass
    public void teardown() {
        if ( webTarget != null ) {
            webTarget = null;
        }
        if ( client != null ) {
            client.close();
            client = null;
        }
    }

    @BeforeTest(enabled = true)
    public void startServer() throws Exception {
        server = new JettyServer();
        server.start( false );
    }

    @AfterTest(enabled = true)
    public void stopServer() throws Exception {
        if ( server != null ) {
            server.shutdown();
        }
    }
}
