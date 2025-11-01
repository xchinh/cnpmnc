package com.example.cnpmnc.services;

import com.example.cnpmnc.enums.UserRole;
import com.nimbusds.jwt.JWTClaimsSet;
import java.util.Map;

public interface IJwtService {
    String generateAccessToken(Long userId, String email, UserRole roles);
    String generateRefreshToken(Long userId, String email, UserRole roles);
    JWTClaimsSet validateToken(String token);
    Map<String, Object> extractClaims(String token);
}
