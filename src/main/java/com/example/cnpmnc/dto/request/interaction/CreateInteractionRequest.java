package com.example.cnpmnc.dto.request.interaction;

import com.example.cnpmnc.enums.InteractionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateInteractionRequest {
    
    @NotNull(message = "Type is required")
    InteractionType type;
    
    @NotBlank(message = "Description is required")
    String description;
    
    @NotNull(message = "Interaction date is required")
    LocalDateTime date;
}