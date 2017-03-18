package com.trizic.api.service;

import java.io.File;
import java.security.ProtectionDomain;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;


public class JettyServer {

    private Server server;
    private String host = "localhost";
    private int port = 8080;
    private File workDir;

    public JettyServer() {
        workDir = new File( System.getProperty( "java.io.tmpdir" ) )
            .toPath()
            .resolve( "jetty-server-" + String.valueOf( System.currentTimeMillis() ) )
            .toFile();
    }

    public JettyServer withHost( String host ) {
        this.host = host;
        return this;
    }

    public JettyServer withPort( int port ) {
        this.port = port;
        return this;
    }

    public JettyServer withWorkingDirectory( File workDir ) {
        this.workDir = workDir;
        return this;
    }

    public void start() throws Exception {
        start( true );
    }

    public void start( boolean blocking ) throws Exception {
        if ( server != null ) {
            throw new IllegalStateException( "Server is already running: need to call stop() first" );
        }

        // create the server
        QueuedThreadPool threadPool = new QueuedThreadPool();
        threadPool.setMaxThreads( 20 );
        server = new Server( threadPool );
        server.setStopAtShutdown( true );

        // HTTP connector
        ServerConnector http = new ServerConnector( server );
        http.setHost( host );
        http.setPort( port );
        http.setIdleTimeout( 30000 );
        server.addConnector( http );

        ProtectionDomain protectionDomain = Main.class.getProtectionDomain();
        String warFile = protectionDomain.getCodeSource().getLocation().toExternalForm();

        // serve web application
        WebAppContext webapp = new WebAppContext( warFile, "/" );
        webapp.setDescriptor( Main.class.getResource( "/WEB-INF/web.xml" ).toString() );
        webapp.setResourceBase( "/" );
        webapp.setTempDirectory( workDir );
        webapp.dumpStdErr();
        workDir.deleteOnExit();

        // enable user transactions
        // TODO: make UserTransaction injectable
        PlusConfiguration plus = new PlusConfiguration();
        plus.bindUserTransaction( webapp );
        plus.configure( webapp );

        // add handlers
        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[] { webapp, new DefaultHandler() } );
        server.setHandler( contexts );

        // start the server
        server.start();

        if ( blocking ) {
            // wait for shutdown
            server.join();
        }
    }

    public void shutdown() throws Exception {
        if ( server != null ) {
            server.stop();
        }
    }
}
