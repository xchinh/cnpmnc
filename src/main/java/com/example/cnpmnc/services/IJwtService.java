package com.example.cnpmnc.services;

import com.nimbusds.jwt.JWTClaimsSet;
import java.util.Map;

public interface IJwtService {
    String generateAccessToken(Map<String, Object> claims);
    String generateRefreshToken(Map<String, Object> claims);
    JWTClaimsSet validateToken(String token);
    Map<String, Object> extractClaims(String token);
}
