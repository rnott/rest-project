package com.trizic.api.service.v1;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider
@Produces(MediaType.TEXT_PLAIN)
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

	@Context
	private UriInfo uriInfo;

	/* 
     * (non-Javadoc)
     * @see javax.ws.rs.ext.ExceptionMapper#toResponse(java.lang.Throwable)
     */
    @Override
    public Response toResponse( NotFoundException e ) {
	    return Response.status( Status.NOT_FOUND ).entity( "Invalid resource path: " + uriInfo.getPath() + "\n" ).build();
    }
}
