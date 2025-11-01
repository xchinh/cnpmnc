package com.example.cnpmnc.services;

import com.example.cnpmnc.dto.request.user.LoginRequest;
import com.example.cnpmnc.dto.request.user.RegisterRequest;
import com.example.cnpmnc.dto.response.access.LoginResponse;
import com.example.cnpmnc.dto.response.access.RegisterResponse;
import org.springframework.stereotype.Service;

@Service
public interface IAccessService {
    LoginResponse login(LoginRequest request);
    RegisterResponse register(RegisterRequest request);
}
