package com.example.cnpmnc.controller.access;

import com.example.cnpmnc.dto.request.user.LoginRequest;
import com.example.cnpmnc.dto.request.user.RegisterRequest;
import com.example.cnpmnc.dto.response.ApiResponse;
import com.example.cnpmnc.dto.response.access.LoginResponse;
import com.example.cnpmnc.services.impl.AccessService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth/")
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
}
