package com.trizic.api.service.v1;

import java.io.IOException;
import java.io.Writer;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter("v1/advisor")
public class AccessControlFilter implements Filter {

    /* 
     * (non-Javadoc)
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain )
        throws IOException, ServletException
    {
        if ( Boolean.getBoolean( "com.trizic.auth.disable" ) ) {
            // allow api key requirement to be overridden
            chain.doFilter( request, response );
            return;
        }

        // validate our token
        // Authorization: Bearer <token>

        String authorization = ((HttpServletRequest) request).getHeader( "Authorization" );
        if ( authorization == null ) {
            // missing required header
            respondAuthorizationRequired( (HttpServletResponse) response );
            return;
        }

        String [] terms = authorization.split( " " );

        if ( terms.length == 0 ) {
            // invalid content
            respondAuthorizationRequired( (HttpServletResponse) response );
            return;
        }

        String type = terms[0];
        if ( ! "Bearer".equals( type ) ) {
            // invalid type
            respondAuthorizationRequired( (HttpServletResponse) response );
            return;
        }

        String token = terms[terms.length - 1];
        String identifier = parseIdFromPath( (HttpServletRequest) request );
        try {
            AccessManager.validateApiToken( token, identifier );
        } catch ( Throwable t ) {
            // invalid token or subject does not match advisor in path
            respondForbidden( (HttpServletResponse) response );
            return;
        }

        // checks out
        chain.doFilter( request, response );
    }

    private String parseIdFromPath( HttpServletRequest request ) {
        String [] segments = request.getPathInfo().split( "/"  );
        // find advisor marker
        int index = 0;
        for ( String s : segments ) {
            if ( "advisor".equals( s ) ) {
                break;
            }
            index++;
        }
        if ( index >= 0 || index < segments.length ) {
            return segments[index + 1];
        }
        return "";
    }

    private void respondAuthorizationRequired( HttpServletResponse response ) throws IOException {
        ((HttpServletResponse) response).setStatus( 401 );
        Writer out = response.getWriter();
        out.write( "API Key required as HTTP header: Authorization: Bearer <apiKey>\n" );
        out.flush();  // commit
    }

    private void respondForbidden( HttpServletResponse response ) throws IOException {
        response.setStatus( 403 );
        Writer out = response.getWriter();
        out.write( "You are not authorized to access this resource\n" );
        out.flush();  // commit
    }

    /* 
     * (non-Javadoc)
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init( FilterConfig config ) throws ServletException {}

    /* 
     * (non-Javadoc)
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {}
}
