package eu.clarin.sru.client.auth;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;

import eu.clarin.sru.client.SRUOperation;
import eu.clarin.sru.client.SRURequestAuthenticator;


public class ClarinFCSRequestAuthenticator implements SRURequestAuthenticator {
    private static final Logger logger =
            LoggerFactory.getLogger(ClarinFCSRequestAuthenticator.class);
    private final AuthenticationInfoProvider authInfoProvider;
    private final String issuer;
    private final Algorithm algorithm;
    private final AtomicLong nextTokenId;
    private final boolean withNotBefore;
    private final long expireTokenTime;


    private ClarinFCSRequestAuthenticator(
            AuthenticationInfoProvider authInfoPovider, String issuer,
            boolean withTokenIds, boolean withNotBefore, long expireTokenTime,
            Algorithm algorithm) {
        this.authInfoProvider = authInfoPovider;
        this.issuer = issuer;

        if (withTokenIds) {
            this.nextTokenId = new AtomicLong((new SecureRandom()).nextLong());
        } else {
            this.nextTokenId = null;
        }
        this.withNotBefore = withNotBefore;
        this.expireTokenTime = expireTokenTime;
        this.algorithm = algorithm;
    }


    @Override
    public String createAuthenticationHeaderValue(SRUOperation operation,
            String endpointURI) {
        switch (operation) {
        case SEARCH_RETRIEVE:
            String audience = authInfoProvider.getAudience(endpointURI);
            if (audience == null) {
                audience = endpointURI;
            }
            JWTCreator.Builder builder = JWT.create()
                .withIssuer(issuer)
                .withAudience(audience);
            if (nextTokenId != null) {
                builder.withJWTId(Long.toHexString(nextTokenId.getAndIncrement()));
            }
            Instant now = Instant.now();
            Date nowDate = Date.from(now);
            builder.withIssuedAt(nowDate);
            if (withNotBefore) {
                builder.withNotBefore(nowDate);
            }
            if (expireTokenTime > -1) {
                Date expireDate = Date.from(now.plusSeconds(expireTokenTime));
                builder.withExpiresAt(expireDate);
            }

            builder.withSubject(authInfoProvider.getSubject(endpointURI));
            String token = builder.sign(algorithm);
            logger.debug("using JWT token: {}", token);
            return "Bearer " + token;
        default:
            return null;
        }
    }


    public interface AuthenticationInfoProvider {
        public String getAudience(String endpointURI);

        public String getSubject(String endpointURI);
    }


    public static class Builder {
        private String issuer;
        private Algorithm algorithm;
        private AuthenticationInfoProvider provider;
        private boolean withTokenIds = true;
        private boolean withNotBefore = true;
        private long expireTokenTime = -1;

        private Builder() {
            /* hide constructor */
        }

        public Builder withIssuer(String issuer) {
            if ((issuer == null) || issuer.isEmpty()) {
                throw new IllegalArgumentException("issuer invalid");
            }
            this.issuer = issuer;
            return this;
        }


        public Builder withAuthenticationInfoProvider(
                AuthenticationInfoProvider provider) {
            if (provider == null) {
                throw new IllegalArgumentException("provider == null");
            }
            this.provider = provider;
            return this;
        }


        public Builder withTokenIds(boolean withTokenIds) {
            this.withTokenIds = withTokenIds;
            return this;
        }


        public Builder withNotBefore(boolean withNotBefore) {
            this.withNotBefore = withNotBefore;
            return this;
        }


        public Builder withExpireTokens(long expireTokenTime) {
            if (expireTokenTime < 0) {
                this.expireTokenTime = -1;
            } else {
                this.expireTokenTime = expireTokenTime;
            }
            return this;
        }


        public Builder withKeyPair(String publicKeyFileName,
                String privateKeyFileName) {
            return withKeyPair(new File(publicKeyFileName),
                    new File(privateKeyFileName));
        }


        public Builder withKeyPair(File publicKeyFile, File privateKeyFile) {
            try {
                logger.debug("load public key from file '{}'", publicKeyFile);
                RSAPublicKey publicKey = readPublicKey(publicKeyFile);
                logger.debug("load private key from file '{}'", privateKeyFile);
                RSAPrivateKey privateKey = readPrivateKey(privateKeyFile);

                algorithm = Algorithm.RSA256(publicKey, privateKey);
                return this;
            } catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException e) {
                throw new IllegalArgumentException("error reading key pair", e);
            }
        }


        public ClarinFCSRequestAuthenticator build() {
            return new ClarinFCSRequestAuthenticator(provider,
                    issuer,
                    withTokenIds,
                    withNotBefore,
                    expireTokenTime,
                    algorithm);
        }


        public static Builder create() {
            return new Builder();
        }
    } // inner class Builder


    private static RSAPublicKey readPublicKey(File file) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        KeyFactory factory = KeyFactory.getInstance("RSA");

        try (FileReader keyReader = new FileReader(file);
          PemReader pemReader = new PemReader(keyReader)) {

            PemObject pemObject = pemReader.readPemObject();
            byte[] content = pemObject.getContent();
            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(content);
            return (RSAPublicKey) factory.generatePublic(pubKeySpec);
        }
    }


    private static RSAPrivateKey readPrivateKey(File file) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        KeyFactory factory = KeyFactory.getInstance("RSA");

        try (FileReader keyReader = new FileReader(file);
          PemReader pemReader = new PemReader(keyReader)) {

            PemObject pemObject = pemReader.readPemObject();
            byte[] content = pemObject.getContent();
            PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(content);
            return (RSAPrivateKey) factory.generatePrivate(privKeySpec);
        }
    }

}
