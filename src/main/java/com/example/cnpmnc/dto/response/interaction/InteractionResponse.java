package com.example.cnpmnc.dto.response.interaction;

import com.example.cnpmnc.enums.InteractionType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InteractionResponse {
    Long id;
    Long customerId;
    InteractionType type;
    String description;
    LocalDateTime date;
    Long userId;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}