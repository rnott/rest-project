package com.trizic.api.service.v1;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider
@Produces(MediaType.APPLICATION_JSON)
public class NoSuchAdvisorExceptionMapper implements ExceptionMapper<NoSuchAdvisorException> {

	/* 
     * (non-Javadoc)
     * @see javax.ws.rs.ext.ExceptionMapper#toResponse(java.lang.Throwable)
     */
    @Override
    public Response toResponse( NoSuchAdvisorException e ) {
	    return Response
	        .status( Status.NOT_FOUND )
	        .entity( new RequestError( e.getErrorCode() ) )
	        .build();
    }
}
