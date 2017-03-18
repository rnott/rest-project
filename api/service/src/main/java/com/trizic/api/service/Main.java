package com.trizic.api.service;

import java.io.File;
import java.util.UUID;


/**
 * 
 * TODO: document Main
 * @see http://git.eclipse.org/c/jetty/org.eclipse.jetty.project.git/tree/examples/embedded/src/main/java/org/eclipse/jetty/embedded/LikeJettyXml.java
 */
public class Main {

	public static final void main( String [] args ) throws Throwable {

	    JettyServer server = new JettyServer();

		for ( int i = 0; i < args.length; i++ ) {
			if ( "--port".equals( args[i] ) ) {
			    // use custom port
				try {
					server.withPort( Integer.parseInt( args[++i] ) );
				} catch ( Throwable ignore ) {
					System.err.println( "Invalid port specified: " + args[i] );
					System.exit( -1 );
				}
			}
		}

		if ( System.getProperties().contains( "trizic.api.data" ) ) {
		    File f = new File( System.getProperty( "trizic.api.data" ) + "-" + UUID.randomUUID().toString() );
		    f.deleteOnExit();
		    f.mkdirs();
		    server.withWorkingDirectory( f );
		}

		new JettyServer().start();
	}
}
