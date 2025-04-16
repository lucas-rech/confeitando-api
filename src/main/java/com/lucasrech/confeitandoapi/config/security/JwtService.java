package com.lucasrech.confeitandoapi.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;

@Service
public class JwtService {
    ZoneOffset offset = ZoneOffset.of("-03:00");
    @Value("${secret.key}")
    private String secret;

    @Value("${jwt.issuer}")
    private String issuer;



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
        return Instant.now().plusSeconds(7200).atZone(offset).toInstant();
    }


}
