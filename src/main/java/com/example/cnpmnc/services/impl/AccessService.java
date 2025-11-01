package com.example.cnpmnc.services.impl;

import com.example.cnpmnc.dto.request.user.LoginRequest;
import com.example.cnpmnc.dto.request.user.RegisterRequest;
import com.example.cnpmnc.dto.response.access.LoginResponse;
import com.example.cnpmnc.dto.response.access.RegisterResponse;
import com.example.cnpmnc.entity.User;
import com.example.cnpmnc.exception.*;
import com.example.cnpmnc.mapper.UserMapper;
import com.example.cnpmnc.repository.UserRepository;
import com.example.cnpmnc.services.IAccessService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccessService implements IAccessService {

    UserRepository userRepository;
    UserMapper userMapper;
    JwtService jwtService;

    @Override
    public LoginResponse login(LoginRequest request) {
        User findUser = userRepository.findByEmail(request.getEmail())
                                        .orElseThrow(() -> new NotFoundException("User not found"));

        if(!findUser.getIsActive()) {
            throw new ForbiddenRequestException("Access denied. User account is inactive");
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        if (!passwordEncoder.matches(request.getPassword(), findUser.getPasswordHash())) {
            throw new UnauthorizedException("Invalid password");
        }

        String accessToken = jwtService.generateAccessToken(Map.of(
                "userId", findUser.getId(),
                "email", findUser.getEmail(),
                "role", findUser.getRole())
        );
        String refreshToken = jwtService.generateRefreshToken(Map.of(
                "userId", findUser.getId(),
                "email", findUser.getEmail(),
                "role", findUser.getRole())
        );

        return userMapper.toLoginResponse(findUser, accessToken, refreshToken);
    }

    @Override
    public RegisterResponse register(RegisterRequest request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictRequestException("Email already exists");
        }

        User user = userMapper.toUser(request);
        PasswordEncoder passwordEncoder= new BCryptPasswordEncoder(10);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        return userMapper.toUserResponse(userRepository.save(user));
    }
}
