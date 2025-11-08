package com.example.cnpmnc.middleware;

import com.example.cnpmnc.exception.UnauthorizedException;
import com.example.cnpmnc.services.IJwtService;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtVerifyFilter extends OncePerRequestFilter {

    private final IJwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        // Skip authentication for public endpoints
        if (isPublicEndpoint(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = resolveToken(request);

            if (token == null) {
                throw new UnauthorizedException("Access token is required");
            }

            // Validate token và lấy claims
            JWTClaimsSet claimsSet = jwtService.validateToken(token);
            
            // Lấy payload từ claims
            Map<String, Object> payload = (Map<String, Object>) claimsSet.getClaim("payload");

            if (payload == null) {
                throw new UnauthorizedException("Invalid token payload");
            }

            // ✅ Đính payload lên request
            request.setAttribute("user", payload);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            payload,
                            null,
                            List.of() // authorities nếu có role
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.debug("User authenticated: userId={}, email={}", 
                     payload.get("id"), payload.get("email"));

        } catch (RuntimeException e) {
            log.error("JWT authentication failed: {}", e.getMessage());
            
            String errorMessage;
            if (e.getMessage().contains("expired")) {
                errorMessage = "Access token has expired";
            } else if (e.getMessage().contains("Invalid signature")) {
                errorMessage = "Invalid token signature";
            } else if (e.getMessage().contains("Invalid token")) {
                errorMessage = "Malformed access token";
            } else {
                errorMessage = "Authentication failed";
            }
            
            throw new UnauthorizedException(errorMessage);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Lấy token từ Authorization header
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * Kiểm tra endpoint có phải là public không
     */
    private boolean isPublicEndpoint(HttpServletRequest request) {
        String originalPath = request.getRequestURI();
        String contextPath = request.getContextPath();
        String path = originalPath;
        
        log.info("Original URI: {}, Context Path: {}", originalPath, contextPath);
        
        // Remove context path from URI (/api/v1)
        if (contextPath != null && !contextPath.isEmpty()) {
            path = path.substring(contextPath.length());
        }

        log.info("Final path for check: {}", path);

        // Public endpoints - không cần authentication
        boolean isPublic = path.equals("/auth/login") ||
               path.equals("/auth/register") ||
               path.equals("/auth/refresh-token") ||
               path.startsWith("/public/") ||
               path.equals("/swagger-ui.html") ||
               path.startsWith("/swagger-ui/") ||
               path.startsWith("/api-docs") ||
               path.startsWith("/v3/api-docs") ||
               path.equals("/favicon.ico") ||
               path.startsWith("/webjars/") ||
               path.startsWith("/actuator/");
               
        log.info("Is public endpoint: {}", isPublic);
        return isPublic;
    }
}