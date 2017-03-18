package com.trizic.api.service.v1;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.UUID;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("v1/registration")
public class RegistrationResource {

    private Logger logger = LoggerFactory.getLogger( AdvisorResource.class );

    @Context
    private UriInfo uriInfo;
    
    @POST
    @Path("advisors")
    @Consumes(MediaType.WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerAdvisor() throws GeneralSecurityException, IOException {
        String identifier = UUID.randomUUID().toString();

        // generate a token
        String token = AccessManager.generateApiToken( identifier );
        Registration registration = new Registration( identifier, token );

        // create advisor data directory
        File f = Persistence.Advisors.getStorage( identifier );
        // this check would make sense if the advisor was providing
        // some information rather than this randomly generating it
        if ( f.exists() ) {
            return Response.status( Status.CONFLICT ).entity( "Advisor already exists: " + identifier ).build();
        }

        boolean created = f.mkdirs();
        if ( created ) {
            logger.info( "Created advisor storage: " + f.getAbsolutePath() );
        } else {
            throw new IllegalStateException( "Failed to create storage for advisor: " + identifier );
        }

        logger.info( "Registered advisor: " + identifier );

        return Response
            .created( uriInfo.getRequestUri().resolve( identifier ) )
            .entity( registration )
            .build();
    }
}
