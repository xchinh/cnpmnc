package com.example.cnpmnc.mapper;

import com.example.cnpmnc.dto.request.user.RegisterRequest;
import com.example.cnpmnc.dto.response.access.LoginResponse;
import com.example.cnpmnc.dto.response.access.RegisterResponse;
import com.example.cnpmnc.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(RegisterRequest request);
    RegisterResponse toUserResponse(User user);

    @Mapping(target = "accessToken", source = "accessToken")
    @Mapping(target = "refreshToken", source = "refreshToken")
    LoginResponse toLoginResponse(User user, String accessToken, String refreshToken);
}
