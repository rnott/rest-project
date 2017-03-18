package com.trizic.api.service.v1;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Path("v1/advisor")
public class AdvisorResourceImpl /*implements AdvisorResource*/ {

    private Logger logger = LoggerFactory.getLogger( AdvisorResource.class );

    @PUT
    @Path("{advisorId}/model")
    @Consumes("application/json")
    @Produces("application/json")
    @Valid
    public ModelResponse save(
            @PathParam("advisorId") @NotNull String advisorId,
            @Valid @NotNull @ValidAllocation Model entity
        )
    {
        ModelResponse response =  new ModelWriter( advisorId ).withModel( entity ).write();
        logger.info( "Created or updated advisor model: " + response.getAdvisorId() + ":" + response.getName() );
        return response;
    }

    @GET
    @Path("{advisorId}/model")
    @Produces("application/json")
    @Valid
    public ModelPage fetch(
            @PathParam("advisorId") @NotNull String advisorId,
            @QueryParam("pageNumber") @Min(1) @DefaultValue("1") Integer pageNumber,
            @QueryParam("pageSize") @Min(1) @DefaultValue("20") Integer pageSize
        )
    {
        ModelReader reader = new ModelReader( advisorId )
            .withPageNumber( pageNumber )
            .withPageSize( pageSize );

        ModelPage page = new ModelPage()
            .withPage( reader.fetch() )
            .withNumberOfPages( reader.getNumberOfPages() )
            .withPageNumber( reader.getPageNumber() )
            .withPageSize( reader.getPageSize() )
            .withTotalNumberOfElements( reader.getTotalNumberOfElements() );
        addLinks( page );

        return page;
    }

    @Context
    private UriInfo uriInfo;

    @Context
    private HttpServletResponse response;

    private void addLinks( ModelPage page ) {
        // add HATEOS link headers
        Map<String, URI> links = new HashMap<String, URI>( 4 );
        int size = page.getPageSize();

        // link to first page
        int number = 1;
        links.put( "first",
            uriInfo.getRequestUriBuilder()
                .queryParam( "pageSize", size )
                .queryParam( "pageNumber", number )
                .build()
        );

        // link to last page
        number = page.getNumberOfPages();        
        links.put( "last",
            uriInfo.getRequestUriBuilder()
                .queryParam( "pageSize", size )
                .queryParam( "pageNumber", number )
                .build()
        );

        // link to previous page
        number = Math.max( 1, page.getPageNumber() - 1 );
        links.put( "prev",
            uriInfo.getRequestUriBuilder()
                .queryParam( "pageSize", size )
                .queryParam( "pageNumber", number )
                .build()
        );

        // link to next page
        number = Math.min( page.getPageNumber() + 1, page.getNumberOfPages() );
        links.put( "next",
            uriInfo.getRequestUriBuilder()
                .queryParam( "pageSize", size )
                .queryParam( "pageNumber", number )
                .build()
        );

        StringBuilder sb = new StringBuilder();
        for ( Map.Entry<String, URI> entry : links.entrySet() ) {
            if ( sb.length() > 0 ) {
                sb.append( ", " );
            }
            sb.append( "<" ).append( entry.getValue().toString() ).append( ">; rel=\"" )
                .append( entry.getKey() ).append( "\"" );
        }

        response.setHeader( "Link", sb.toString() );
    }
}
