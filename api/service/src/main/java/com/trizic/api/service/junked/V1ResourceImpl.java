package com.trizic.api.service.junked;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.logging.Logger;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import com.trizic.api.model.ModelPage;
import com.trizic.api.model.ModelRequest;
import com.trizic.api.resource.V1Resource;
import com.trizic.api.service.v1.ModelReader;


public class V1ResourceImpl /*implements V1Resource*/ {

    private Logger logger = Logger.getLogger( "V1Resource" );

    /* 
     * (non-Javadoc)
     * @see com.trizic.api.resource.V1Resource#putV1AdvisorByAdvisorIdModel(java.lang.String, com.trizic.api.model.ModelRequest)
    @Override
    public PutV1AdvisorByAdvisorIdModelResponse putV1AdvisorByAdvisorIdModel(
            String advisorId, ModelRequest entity ) throws Exception {
        return null;
    }
     */

    /* 
     * (non-Javadoc)
     * @see com.trizic.api.resource.V1Resource#getV1AdvisorByAdvisorIdModel(java.lang.String, java.lang.Integer, java.lang.Integer)
    @Override
    public GetV1AdvisorByAdvisorIdModelResponse getV1AdvisorByAdvisorIdModel(
            String advisorId, Integer pageNumber, Integer pageSize )
            throws Exception
    {
        ModelReader reader;
        try {
            reader = new ModelReader( advisorId )
                .withPageNumber( pageNumber )
                .withPageSize( pageSize );

        } catch ( IllegalArgumentException e ) {
            // {"errorCode": "advisor.not.found"}
            logger.warning( "Adivsor not found: " + advisorId );
            StreamingOutput entity = new StreamingOutput() {
                @Override
                public void write( OutputStream out ) throws IOException, WebApplicationException {
                    Writer writer = new BufferedWriter( new OutputStreamWriter( out ) );
                    writer.write("{\"errorCode\": \"advisor.not.found\"}");
                }
            };
            return GetV1AdvisorByAdvisorIdModelResponse.withJsonNotFound( entity );
        }

        return GetV1AdvisorByAdvisorIdModelResponse.withJsonOK(
            new ModelPage()
                .withPage( (List)reader.fetch() )
                .withNumberOfPages( reader.getNumberOfPages() )
                .withPageNumber( reader.getPageNumber() )
                .withPageSize( reader.getPageSize() )
                .withTotalNumberOfElements( reader.getTotalNumberOfElements() )
        );
    }
     */
}
