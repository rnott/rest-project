package com.trizic.api.service.v1;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.trizic.api.service.v1.Persistence.Advisors;

/**
 * Component to read advisor model data. The reader is configured using
 * the builder pattern. If any of the configuration values are invalid,
 * an <code>IllegalArgumentException</code> is raised.
 */
public class ModelReader {

    private final String advisor;
    private final ObjectMapper mapper;

    private int pageSize = 20;
    private int pageNumber = 1;
    private File [] files;

    public ModelReader( String advisor ) {
        if ( advisor == null || advisor.length() == 0 ) {
            throw new NoSuchAdvisorException( advisor );
        }
        File f = Persistence.Advisors.getStorage( advisor );
        if ( ! f.exists() ) {
            throw new NoSuchAdvisorException( advisor );
        }
        this.advisor = advisor;
        files = list();

        mapper = new ObjectMapper();
        mapper.configure( Feature.AUTO_CLOSE_SOURCE, true )
            .configure( MapperFeature.AUTO_DETECT_FIELDS, true )
            .configure( SerializationFeature.INDENT_OUTPUT, true );
    }

    public int getPageSize() {
        return pageSize;
    }

    public ModelReader withPageSize( int pageSize ) {
        if ( pageSize < 1 ) {
            throw new IllegalArgumentException( "Page size must be greater than zero" );
        }
        this.pageSize = pageSize;
        return this;
    }

    public String getAdvisor() {
        return advisor;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public ModelReader withPageNumber( int pageNumber ) {
        if ( pageNumber < 1 ) {
            throw new IllegalArgumentException( "Page number must be greater than zero" );
        }
        this.pageNumber = pageNumber;
        return this;
    }

    public int getTotalNumberOfElements() {
        return files.length;
    }

    public int getNumberOfPages() {
        if ( files.length == 0 ) {
            return 0;
        }
        return files.length / (pageSize + 1) + 1;
    }

    public List<ModelResponse> fetch() {
        int start = (pageNumber - 1) * pageSize;
        int end = Math.min( start + pageSize, files.length );
        List<ModelResponse> entries = new ArrayList<ModelResponse>( end - start );

        for ( int i = start; i < end; i++ ) {
            try {
                entries.add( mapper.readValue( files[i], ModelResponse.class ) );
            } catch ( Throwable t ) {
                // parse exceptions are internal issues
                throw new IllegalStateException( "Failed to read model for advisor: " + advisor + "/" + files[i].getName(), t );
            }
        }
        return entries;
    }

    private File [] list() {
        return Advisors.getStorage( advisor ).listFiles( new FileFilter() {
            @Override
            public boolean accept( File pathname ) {
                // exclude dot files which may be used as metadata
                return ! pathname.getName().startsWith( Persistence.METADATA_PREFIX );
            }
        });
    }
}
