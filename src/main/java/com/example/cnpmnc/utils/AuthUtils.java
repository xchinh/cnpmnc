package com.example.cnpmnc.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;

public class AuthUtils {
    
    public static Long getCurrentUserId() {
        Map<String, Object> payload = getCurrentPayload();
        if (payload != null && payload.get("userId") != null) {
            Object id = payload.get("userId");
            if (id instanceof Number) {
                return ((Number) id).longValue();
            }
            return Long.valueOf(id.toString());
        }
        return null;
    }
    
    public static String getCurrentUserEmail() {
        Map<String, Object> payload = getCurrentPayload();
        if (payload != null && payload.get("email") != null) {
            return payload.get("email").toString();
        }
        return null;
    }
    
    public static String getCurrentUserRole() {
        Map<String, Object> payload = getCurrentPayload();
        if (payload != null && payload.get("role") != null) {
            return payload.get("role").toString();
        }
        return null;
    }
    
    public static String getCurrentUsername() {
        Map<String, Object> payload = getCurrentPayload();
        if (payload != null && payload.get("username") != null) {
            return payload.get("username").toString();
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getCurrentPayload() {
        HttpServletRequest request = getCurrentRequest();
        return (Map<String, Object>) request.getAttribute("user");
    }
    
    private static HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            throw new IllegalStateException("No current HTTP request found");
        }
        return attrs.getRequest();
    }
}