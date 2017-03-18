package com.trizic.api.service.v1;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccessManager {

    private static final Logger logger = LoggerFactory.getLogger( AccessManager.class );
    private static final String API_KEY = "apiKey";

    private static KeyStore keystore;
    private static String apiKey;

    public static String generateApiToken( String identifier ) throws GeneralSecurityException, IOException {
        //  signature algorithm
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
     
        Date now = new Date();
     
        // sign with secret
        byte [] apiKeySecretBytes = DatatypeConverter.parseBase64Binary( getApiKey() );

        Key signingKey = new SecretKeySpec( apiKeySecretBytes, signatureAlgorithm.getJcaName() );
     
        // set claims
        // no expiration for demo
        JwtBuilder builder = Jwts.builder()
            .setId( identifier )
            .setIssuedAt( now )
            .setSubject( identifier )
            .setIssuer( "com.trizic.api.v1")
            .signWith( signatureAlgorithm, signingKey );
     
        // build and serialize to be URL-safe
        logger.info( "Create API token for identifier: " + identifier );
        return builder.compact();
    }

    public static void validateApiToken( String token, String identifier ) throws JwtException, GeneralSecurityException, IOException {
        Claims claims = Jwts.parser()         
            .setSigningKey( DatatypeConverter.parseBase64Binary( getApiKey() ) )
            .parseClaimsJws( token ).getBody();
        if ( ! claims.getId().equals( identifier ) ) {
            throw new GeneralSecurityException( "Token does not assert claimed identity: " + identifier );
        }
        if ( ! claims.getSubject().equals( identifier ) ) {
            throw new GeneralSecurityException( "Token does not assert claimed subject: " + identifier );
        }
        if ( ! claims.getIssuer().equals( "com.trizic.api.v1" ) ) {
            throw new GeneralSecurityException( "The token is not from a trusted issuer: " + claims.getIssuer() );
        }
        if ( claims.getIssuedAt().after( new Date() ) ) {
            throw new GeneralSecurityException( "Token was created at a future date: " + claims.getIssuedAt() );
        }
    }

    private static KeyStore getKeyStore() throws IOException, GeneralSecurityException {
        if ( keystore != null ) {
            return keystore;
        }

        // load it
        keystore = KeyStore.getInstance( "JCEKS" );  // this type makes secret keys work
        File root = Persistence.Advisors.getStorage();
        File store = new File( root, ".keystore" );
        if ( store.exists() ) {
            // load
            FileInputStream in = new FileInputStream( store );
            try {
                keystore.load( in, "insecure".toCharArray() );
                logger.info( "Using existing keystore: " + store.getAbsolutePath() );
            } finally {
                try {
                    in.close();
                } catch ( Throwable ignore ) {}
            }

        } else {
            // create
            keystore.load( null, "insecure".toCharArray() );

            // create a secret
            // this is NOT buttoned down, simply for demo
            KeyGenerator generator = KeyGenerator.getInstance( "AES" );
            generator.init( 256, SecureRandom.getInstance( "SHA1PRNG" ) );
            SecretKey secretKey = generator.generateKey();
            keystore.setKeyEntry( API_KEY, secretKey, "insecure".toCharArray(), null );

            // store
            FileOutputStream out = new FileOutputStream( store );
            try {
                keystore.store( out, "insecure".toCharArray() );
                logger.info( "Initialized new keystore: " + store.getAbsolutePath() );
            } finally {
                try {
                    out.close();
                } catch ( Throwable ignore ) {}
            }
        }

        return keystore;
    }

    private static String getApiKey() throws GeneralSecurityException, IOException {
        if ( apiKey == null ) {
            SecretKey key = (SecretKey) getKeyStore().getKey( API_KEY, "insecure".toCharArray() );
            apiKey = Base64.getEncoder().encodeToString( key.getEncoded() );
        }
        return apiKey;
    }
}
