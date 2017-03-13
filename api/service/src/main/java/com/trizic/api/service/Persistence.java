package com.trizic.api.service;

import java.io.File;
import java.nio.file.Path;


public interface Persistence {

    static class Advisors {
        static final Path ROOT;
        static {
            File f;
            if ( System.getProperties().containsKey( "trizic.api.io.root" ) ) {
                f = new File( System.getProperty( "trizic.api.io.root" ) );
            } else {
                f = new File( System.getProperty( "java.io.tmpdir" ) );
            }
            ROOT = f.toPath().resolve( "advisors" );
            if ( ROOT.toFile().mkdirs() ) {
                System.out.println( "Created advisor data directory: " + ROOT );
            } else {
                System.out.println( "Using advisor data directory: " + ROOT );
            }
        }
    }
}
