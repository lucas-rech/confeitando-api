package com.lucasrech.confeitandoapi.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneOffset;

@Component
public class JwtService {

    // UTC -03:00
    private final ZoneOffset offset = ZoneOffset.of("-03:00");

    @Value("${secret.key}")
    private String secret;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.expiration}")
    private long expirationTime;

    public String generateToken(String user) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withIssuer(issuer)
                .withSubject(user)
                .withIssuedAt(creationDate())
                .withExpiresAt(expirationDate())
                .sign(algorithm);

    }

    public String validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.require(algorithm)
                .withIssuer(issuer)
                .build()
                .verify(token)
                .getSubject();
    }

    private Instant creationDate() {
        return Instant.now().atZone(offset).toInstant();
    }

    private Instant expirationDate() {
        return Instant.now().plusSeconds(expirationTime).atZone(offset).toInstant();
    }


}
