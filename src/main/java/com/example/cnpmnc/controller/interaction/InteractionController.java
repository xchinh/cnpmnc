package com.example.cnpmnc.controller.interaction;

import com.example.cnpmnc.dto.request.interaction.CreateInteractionRequest;
import com.example.cnpmnc.dto.request.interaction.UpdateInteractionRequest;
import com.example.cnpmnc.dto.response.ApiResponse;
import com.example.cnpmnc.dto.response.interaction.InteractionListResponse;
import com.example.cnpmnc.dto.response.interaction.InteractionResponse;
import com.example.cnpmnc.enums.InteractionType;
import com.example.cnpmnc.services.IInteractionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/customers/{customerId}/interactions")
@RequiredArgsConstructor
@Tag(name = "Interaction", description = "Customer Interaction Management APIs")
public class InteractionController {
    
    private final IInteractionService interactionService;
    
    @GetMapping
    @Operation(summary = "Get customer interactions", description = "Get list of interactions for a specific customer")
    public ApiResponse<InteractionListResponse> getInteractions(
            @PathVariable Long customerId,
            @Parameter(description = "Filter by interaction type")
            @RequestParam(required = false) InteractionType type,
            @Parameter(description = "Filter from date (yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "Filter to date (yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        InteractionListResponse response = interactionService.getInteractionsByCustomerId(
                customerId, type, startDate, endDate);
        
        return ApiResponse.<InteractionListResponse>builder()
                .status(HttpStatus.OK)
                .message("Interactions retrieved successfully")
                .code("2000")
                .metadata(Map.of("data", response))
                .build();
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get interaction details", description = "Get details of a specific interaction")
    public ApiResponse<InteractionResponse> getInteraction(
            @PathVariable Long customerId,
            @PathVariable Long id) {
        
        InteractionResponse response = interactionService.getInteractionById(customerId, id);
        
        return ApiResponse.<InteractionResponse>builder()
                .status(HttpStatus.OK)
                .message("Interaction retrieved successfully")
                .code("2001")
                .metadata(Map.of("data", response))
                .build();
    }
    
    @PostMapping
    @Operation(summary = "Create new interaction", description = "Create a new interaction for customer")
    public ApiResponse<InteractionResponse> createInteraction(
            @PathVariable Long customerId,
            @Valid @RequestBody CreateInteractionRequest request) {
        
        InteractionResponse response = interactionService.createInteraction(customerId, request);
        
        return ApiResponse.<InteractionResponse>builder()
                .status(HttpStatus.CREATED)
                .message("Interaction created successfully")
                .code("2002")
                .metadata(Map.of("data", response))
                .build();
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update interaction", description = "Update an existing interaction")
    public ApiResponse<InteractionResponse> updateInteraction(
            @PathVariable Long customerId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateInteractionRequest request) {
        
        InteractionResponse response = interactionService.updateInteraction(customerId, id, request);
        
        return ApiResponse.<InteractionResponse>builder()
                .status(HttpStatus.OK)
                .message("Interaction updated successfully")
                .code("2003")
                .metadata(Map.of("data", response))
                .build();
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete interaction", description = "Delete an interaction (soft delete)")
    public ApiResponse<?> deleteInteraction(
            @PathVariable Long customerId,
            @PathVariable Long id) {
        
        interactionService.deleteInteraction(customerId, id);
        
        return ApiResponse.builder()
                .status(HttpStatus.OK)
                .message("Interaction deleted successfully")
                .code("2004")
                .metadata(Map.of("message", "Interaction deleted successfully"))
                .build();
    }
}