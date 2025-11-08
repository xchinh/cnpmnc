package com.example.cnpmnc.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefreshRequest {
    @NotBlank(message = "Refresh token is required")
    String refreshToken;
}
