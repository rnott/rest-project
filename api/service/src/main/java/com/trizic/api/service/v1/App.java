package com.trizic.api.service.v1;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

//@ApplicationPath("v1")
public class App extends Application {

    /* 
     * (non-Javadoc)
     * @see javax.ws.rs.core.Application#getClasses()
     */
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<Class<?>>();
        resources.add( RegistrationResource.class );
        resources.add( AdvisorResourceImpl.class );
        resources.add( DefaultExceptionMapper.class );
        resources.add( ConstraintViolationExceptionMapper.class );
        resources.add( NoSuchAdvisorExceptionMapper.class );
        resources.add( NotFoundExceptionMapper.class );
        resources.add( NotAllowedExceptionMapper.class );
        

        return Collections.unmodifiableSet( resources );
    }
}
