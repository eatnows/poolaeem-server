package com.poolaeem.poolaeem.common.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.poolaeem.poolaeem.common.exception.jwt.ExpiredTokenException;
import com.poolaeem.poolaeem.common.exception.jwt.InvalidTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtTokenUtil {
    private final byte[] PRIVATE_KEY;
    private final byte[] PUBLIC_KEY;
    private final String ISSUER = "poolaeem";
    private final String SUBJECT_AUTHENTICATION = "Authentication";

    private final int ACCESS_TIME = 1000 * 60 * 30;
//    private final int REFRESH_TIME = 1000 * 60 * 60 * 24 * 15;

    public JwtTokenUtil(@Value("${jwt.rsa.private}") String privateKey,
                        @Value("${jwt.rsa.public}") String publicKey) {
        Base64.Decoder base64Encoder = Base64.getMimeDecoder();
        this.PRIVATE_KEY = base64Encoder.decode(privateKey.replace("\\n", "").replaceAll("-{5}[ a-zA-Z]*-{5}", ""));
        this.PUBLIC_KEY = base64Encoder.decode(publicKey.replace("\\n", "").replaceAll("-{5}[ a-zA-Z]*-{5}", ""));
    }

    public String generateAccessToken() {
        return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(SUBJECT_AUTHENTICATION)
                .withClaim("email", "email")
                .withClaim("name", "name")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TIME))
                .sign(generateAlgorithm());
    }

    private Algorithm generateAlgorithm() {
        return Algorithm.RSA512(getPublicKey(), getPrivateKey());
    }

    private RSAPrivateKey getPrivateKey() {
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(PRIVATE_KEY);
            return (RSAPrivateKey) kf.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    private RSAPublicKey getPublicKey() {
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(PUBLIC_KEY);
            return (RSAPublicKey) kf.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<DecodedJWT> validAccessToken(String token) {
        try {
            DecodedJWT decodedJWT = JWT.require(generateAlgorithm()).build().verify(token);

            if (SUBJECT_AUTHENTICATION.equals(decodedJWT.getSubject())) {
                throw new InvalidTokenException();
            }

            return Optional.ofNullable(decodedJWT);
        } catch (TokenExpiredException e) {
            throw new ExpiredTokenException();
        } catch (Exception e) {
            throw new InvalidTokenException();
        }
    }
}
