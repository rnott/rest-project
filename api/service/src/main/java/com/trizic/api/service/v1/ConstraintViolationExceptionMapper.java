package com.trizic.api.service.v1;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ElementKind;
import javax.validation.Path.Node;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider
@Produces(MediaType.APPLICATION_JSON)
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    /* 
     * (non-Javadoc)
     * @see javax.ws.rs.ext.ExceptionMapper#toResponse(java.lang.Throwable)
     */
    @Override
    public Response toResponse( ConstraintViolationException e ) {
        Set<RequestError> errors = new HashSet<RequestError>();
        for ( ConstraintViolation<?> violation : e.getConstraintViolations() ) {
            Annotation constraint = violation.getConstraintDescriptor().getAnnotation();
            if ( constraint instanceof NotNull || constraint instanceof Min || constraint instanceof Max ) {
                errors.add( new RequestError( toKey( violation ) ) );
            } else {
                errors.add( new RequestError( violation.getMessage() ) );
            }
        }
        // spec only provides for a single error, we'll choose the first one
        // a considerate response would list all
        // a considerate response might also include the offending value
        // and a human readable message: this is all easily possible here
	    return Response.status( Status.BAD_REQUEST )
	        .entity( errors.iterator().next() )
	        .build();
    }

    private String toKey( ConstraintViolation<?> violation ) {

        StringBuilder path = new StringBuilder();
        Iterator<Node> segments = violation.getPropertyPath().iterator();
        while( segments.hasNext() ) {
            Node n = segments.next();
            if ( n.getKind() == ElementKind.PROPERTY ) {
                if ( path.length() > 0 ) {
                    path.append( '.' );
                }
                path.append( n.getName() );
            }
        }
        if ( path.length() == 0 ) {
            path.append( "request" );
        }
        path.append( ".invalid" );

        return path.toString();
    }
}
