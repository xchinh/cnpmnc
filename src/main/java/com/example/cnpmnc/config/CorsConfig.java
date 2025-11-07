package com.example.cnpmnc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // Cho phép credentials (cookies, authorization headers)
        config.setAllowCredentials(true);

        // IMPORTANT: Khi dùng setAllowCredentials(true), phải dùng setAllowedOrigins thay vì setAllowedOriginPatterns
        // và không thể dùng "*" - phải chỉ định cụ thể từng origin
        config.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",      // React dev
                "http://localhost:5173",      // Vite dev
                "http://localhost:4200",      // Angular dev
                "http://localhost:8080",      // Spring Boot dev
                "http://127.0.0.1:3000",      // React với 127.0.0.1
                "http://127.0.0.1:5173",      // Vite với 127.0.0.1
                "http://127.0.0.1:4200",       // Angular với 127.0.0.1
                // Thêm production domain khi deploy
                "https://cnpmnc.onrender.com"
        ));

        // Cho phép tất cả headers
        config.setAllowedHeaders(Arrays.asList("*"));

        // Cho phép tất cả methods
        config.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS", "HEAD"
        ));

        // Expose headers (để frontend có thể đọc)
        config.setExposedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-Total-Count",
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Credentials"
        ));

        // Cache preflight response trong 1 giờ
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Áp dụng cho tất cả endpoints
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}