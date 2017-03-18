package com.trizic.api.service.v1;

import javax.ws.rs.NotAllowedException;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider
@Produces(MediaType.TEXT_PLAIN)
public class NotAllowedExceptionMapper implements ExceptionMapper<NotAllowedException> {

    /* 
     * (non-Javadoc)
     * @see javax.ws.rs.ext.ExceptionMapper#toResponse(java.lang.Throwable)
     */
    @Override
    public Response toResponse( NotAllowedException e ) {
        return Response.status( 405 ).entity( e.getMessage() + "\n" ).build();
    }
}
