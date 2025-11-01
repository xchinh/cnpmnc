package com.example.cnpmnc.services.impl;

import com.example.cnpmnc.enums.UserRole;
import com.example.cnpmnc.services.IJwtService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JwtService implements IJwtService {

    @Value("${spring.security.oauth2.resource-server.jwt.secret}")
    private String secretKey;

    @Value("${spring.security.oauth2.resource-server.jwt.expiration}")
    private long accessTokenExpiration;

    @Value("${spring.security.oauth2.resource-server.jwt.refresh-expiration}")
    private long refreshTokenExpiration;

    public String generateAccessToken(Long userId, String email, UserRole roles) {
        return generateToken(userId, email, roles, accessTokenExpiration);
    }

    public String generateRefreshToken(Long userId, String email, UserRole roles) {
        return generateToken(userId, email, roles, refreshTokenExpiration);
    }

    private String generateToken(Long userId, String email, UserRole roles, long expirationMillis) {
        try {
            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .subject(String.valueOf(userId))
                    .claim("email", email)
                    .claim("roles", roles)
                    .issueTime(new Date())
                    .expirationTime(new Date(System.currentTimeMillis() + expirationMillis))
                    .build();

            JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
            SignedJWT signedJWT = new SignedJWT(header, claims);
            signedJWT.sign(new MACSigner(secretKey.getBytes()));

            return signedJWT.serialize();
        } catch (Exception e) {
            throw new RuntimeException("Error creating JWT", e);
        }
    }

    public JWTClaimsSet validateToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(secretKey.getBytes());

            if (!signedJWT.verify(verifier)) {
                throw new RuntimeException("Invalid signature");
            }

            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            if (expirationTime.before(new Date())) {
                throw new RuntimeException("Token expired");
            }

            return signedJWT.getJWTClaimsSet();
        } catch (Exception e) {
            throw new RuntimeException("Invalid token", e);
        }
    }

    public Map<String, Object> extractClaims(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getClaims();
        } catch( Exception e) {
            throw new RuntimeException("Invalid token", e);
        }
    }
}
