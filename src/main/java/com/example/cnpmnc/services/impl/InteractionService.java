package com.example.cnpmnc.services.impl;

import com.example.cnpmnc.dto.request.interaction.CreateInteractionRequest;
import com.example.cnpmnc.dto.request.interaction.UpdateInteractionRequest;
import com.example.cnpmnc.dto.response.interaction.InteractionListResponse;
import com.example.cnpmnc.dto.response.interaction.InteractionResponse;
import com.example.cnpmnc.entity.Interaction;
import com.example.cnpmnc.enums.InteractionType;
import com.example.cnpmnc.exception.NotFoundException;
import com.example.cnpmnc.mapper.InteractionMapper;
import com.example.cnpmnc.repository.InteractionRepository;
import com.example.cnpmnc.services.IInteractionService;
import com.example.cnpmnc.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InteractionService implements IInteractionService {
    
    private final InteractionRepository interactionRepository;
    private final InteractionMapper interactionMapper;
    
    @Override
    public InteractionListResponse getInteractionsByCustomerId(
            Long customerId, 
            InteractionType type, 
            LocalDateTime startDate, 
            LocalDateTime endDate) {
        
        List<Interaction> interactions = interactionRepository.findByCustomerIdWithFilters(
                customerId, type, startDate, endDate);
        
        Long total = interactionRepository.countByCustomerIdWithFilters(
                customerId, type, startDate, endDate);
        
        List<InteractionResponse> data = interactions.stream()
                .map(interactionMapper::toInteractionResponse)
                .collect(Collectors.toList());
        
        return InteractionListResponse.builder()
                .data(data)
                .total(total)
                .build();
    }
    
    @Override
    public InteractionResponse getInteractionById(Long customerId, Long id) {
        Interaction interaction = interactionRepository
                .findByIdAndCustomerIdAndDeletedAtIsNull(id, customerId)
                .orElseThrow(() -> new NotFoundException("Interaction not found"));
        
        return interactionMapper.toInteractionResponse(interaction);
    }
    
    @Override
    @Transactional
    public InteractionResponse createInteraction(Long customerId, CreateInteractionRequest request) {
        Long currentUserId = AuthUtils.getCurrentUserId();
        
        Interaction interaction = interactionMapper.toInteraction(request);
        interaction.setCustomerId(customerId);
        interaction.setUserId(currentUserId);
        
        Interaction saved = interactionRepository.save(interaction);
        
        log.info("Created new interaction for customer {} by user {}", customerId, currentUserId);
        
        return interactionMapper.toInteractionResponse(saved);
    }
    
    @Override
    @Transactional
    public InteractionResponse updateInteraction(Long customerId, Long id, UpdateInteractionRequest request) {
        Interaction interaction = interactionRepository
                .findByIdAndCustomerIdAndDeletedAtIsNull(id, customerId)
                .orElseThrow(() -> new NotFoundException("Interaction not found"));
        
        interaction.setType(request.getType());
        interaction.setDescription(request.getDescription());
        interaction.setInteractionDate(request.getDate());
        
        Interaction updated = interactionRepository.save(interaction);
        
        log.info("Updated interaction {} for customer {}", id, customerId);
        
        return interactionMapper.toInteractionResponse(updated);
    }
    
    @Override
    @Transactional
    public void deleteInteraction(Long customerId, Long id) {
        Interaction interaction = interactionRepository
                .findByIdAndCustomerIdAndDeletedAtIsNull(id, customerId)
                .orElseThrow(() -> new NotFoundException("Interaction not found"));
        
        interaction.setDeletedAt(LocalDateTime.now());
        interactionRepository.save(interaction);
        
        log.info("Deleted interaction {} for customer {}", id, customerId);
    }

}