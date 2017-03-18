package com.trizic.api.service.v1;

import java.io.File;
import java.nio.file.Path;
import java.util.UUID;


public interface Persistence {

    static final String METADATA_PREFIX = ".";

    static class Advisors {
        private static final Path DATA;
        static {
            File f;
            if ( System.getProperties().containsKey( "trizic.api.data" ) ) {
                f = new File( System.getProperty( "trizic.api.data" ) );
                f.mkdirs();
            } else {
                // use temporary directory
                f = new File( System.getProperty( "java.io.tmpdir" ) + UUID.randomUUID().toString() );
                f.mkdirs();
                f.deleteOnExit();
            }
            DATA = f.toPath().resolve( "advisors" );
            if ( DATA.toFile().mkdirs() ) {
                System.out.println( "Created advisor data directory: " + DATA );
            } else {
                System.out.println( "Using advisor data directory: " + DATA );
            }
        }

        public static File getStorage() {
            return DATA.toFile();
        }

        public static File getStorage( String advisor ) {
            return DATA.resolve( advisor ).toFile();
        }
    }
}
