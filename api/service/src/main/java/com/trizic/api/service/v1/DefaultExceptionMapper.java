package com.trizic.api.service.v1;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Provider
@Produces(MediaType.TEXT_PLAIN)
public class DefaultExceptionMapper implements ExceptionMapper<Throwable> {

	private Logger logger = LoggerFactory.getLogger( DefaultExceptionMapper.class );

	@Context
	private UriInfo info;

	@Context
	private Providers providers;


	/* 
     * (non-Javadoc)
     * @see javax.ws.rs.ext.ExceptionMapper#toResponse(java.lang.Throwable)
     */
    @Override
    @SuppressWarnings( "unchecked" )
    public Response toResponse( Throwable t ) {
    	// discover the deepest specific handler based on the causal chain
    	Response response = null;
    	Throwable cause = t;
    	while ( cause != null ) {
    		ExceptionMapper<Throwable> mapper = (ExceptionMapper<Throwable>) providers.getExceptionMapper( cause.getClass() );
    		if ( mapper != null && mapper.getClass() != this.getClass() ) {
    			response = mapper.toResponse( cause );
    		}
    		cause = cause.getCause();
    	}
    	if ( response != null ) {
    		// handled
    		return response;
    	}

    	// default response
    	logger.error( "Service failed unexpectedly: ", t );
	    return Response.status( Status.SERVICE_UNAVAILABLE ).entity( "Please try again later." ).build();
    }
}
