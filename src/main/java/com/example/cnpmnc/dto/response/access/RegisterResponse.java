package com.example.cnpmnc.dto.response.access;

import com.example.cnpmnc.enums.UserRole;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterResponse {
    String username;
    String email;
    UserRole role;
    LocalDateTime createdAt;
}
