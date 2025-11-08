package com.example.cnpmnc.dto.response.access;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefreshTokenResponse {
    String accessToken;
}
