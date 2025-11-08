package com.example.cnpmnc.controller.access;

import com.example.cnpmnc.dto.request.user.LoginRequest;
import com.example.cnpmnc.dto.request.user.RefreshRequest;
import com.example.cnpmnc.dto.request.user.RegisterRequest;
import com.example.cnpmnc.dto.response.ApiResponse;
import com.example.cnpmnc.services.impl.AccessService;
import com.example.cnpmnc.utils.AuthUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth/")
@Tag(name="Authentication", description="Authentication Management APIs")
@Slf4j
public class AccessController {
    @Autowired
    private AccessService accessService;

    @PostMapping("login")
    public ApiResponse<?> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.builder()
                .status(HttpStatus.OK)
                .message("Login successful")
                .code("1000")
                .metadata(Map.of(
                        "data", accessService.login(request)
                ))
                .build();
    }

    @PostMapping("register")
    public ApiResponse<?> register(@Valid @RequestBody RegisterRequest request) {
        return ApiResponse.builder()
                .status(HttpStatus.CREATED)
                .message("Register successful")
                .code("1001")
                .metadata(Map.of(
                        "data", accessService.register(request)
                ))
                .build();
    }

    @PostMapping("refresh-token")
    public ApiResponse<?> refresh(@Valid @RequestBody RefreshRequest request) {
        return ApiResponse.builder()
                .status(HttpStatus.CREATED)
                .message("Refresh token successful")
                .code("1002")
                .metadata(Map.of(
                        "data",accessService.refresh(request)))
                .build();
    }

    @PostMapping("logout")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "User logout", description = "Logout user (requires authentication)")
    public ApiResponse<?> logout() {
        Long userId = AuthUtils.getCurrentUserId();
        String email = AuthUtils.getCurrentUserEmail();

        return ApiResponse.builder()
                .status(HttpStatus.OK)
                .message("Logout successful")
                .code("1001")
                .metadata(Map.of(
                        "userId", userId,
                        "email", email
                ))
                .build();
    }
}
