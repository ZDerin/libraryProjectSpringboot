package libraryProjectGroup.libraryProject.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.io.InputStream;

@Configuration
public class JwtConfiguration {

    @Value("${security.jwt.keystore-location}")
    private String keyStorePath;

    @Value("${security.jwt.keystore-password}")
    private String keyStorePassword;

    @Value("${security.jwt.key-alias}")
    private String keyAlias;

    @Value("${security.jwt.private-key-passphrase}")
    private String privateKeyPassphrase;

    @Bean
    public KeyStore keyStore() throws Exception {
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(keyStorePath);
        keyStore.load(resourceAsStream, keyStorePassword.toCharArray());
        return keyStore;
    }

    @Bean
    public RSAPrivateKey jwtSigningKey(KeyStore keyStore) throws Exception {
        Key key = keyStore.getKey(keyAlias, privateKeyPassphrase.toCharArray());
        if (key instanceof RSAPrivateKey) {
            return (RSAPrivateKey) key;
        }
        throw new IllegalArgumentException("Unable to load RSA private key");
    }

    @Bean
    public RSAPublicKey jwtValidationKey(KeyStore keyStore) throws Exception {
        java.security.cert.Certificate certificate = keyStore.getCertificate(keyAlias);
        PublicKey publicKey = certificate.getPublicKey();
        if (publicKey instanceof RSAPublicKey) {
            return (RSAPublicKey) publicKey;
        }
        throw new IllegalArgumentException("Unable to load RSA public key");
    }

    @Bean
    public JwtDecoder jwtDecoder(RSAPublicKey rsaPublicKey) {
        return NimbusJwtDecoder.withPublicKey(rsaPublicKey).build();
    }
}
