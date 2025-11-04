package com.example.cnpmnc.services;

import com.example.cnpmnc.dto.request.interaction.CreateInteractionRequest;
import com.example.cnpmnc.dto.request.interaction.UpdateInteractionRequest;
import com.example.cnpmnc.dto.response.interaction.InteractionListResponse;
import com.example.cnpmnc.dto.response.interaction.InteractionResponse;
import com.example.cnpmnc.enums.InteractionType;

import java.time.LocalDateTime;

public interface IInteractionService {
    InteractionListResponse getInteractionsByCustomerId(
            Long customerId, 
            InteractionType type, 
            LocalDateTime startDate, 
            LocalDateTime endDate
    );
    
    InteractionResponse getInteractionById(Long customerId, Long id);
    
    InteractionResponse createInteraction(Long customerId, CreateInteractionRequest request);
    
    InteractionResponse updateInteraction(Long customerId, Long id, UpdateInteractionRequest request);
    
    void deleteInteraction(Long customerId, Long id);
}