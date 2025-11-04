package com.example.cnpmnc.mapper;

import org.mapstruct.Mapper;

import com.example.cnpmnc.dto.response.interaction.InteractionResponse;
import com.example.cnpmnc.dto.request.interaction.CreateInteractionRequest;
import com.example.cnpmnc.entity.Interaction;

@Mapper(componentModel = "spring")
public interface InteractionMapper {
    Interaction toInteraction(CreateInteractionRequest request);
    InteractionResponse toInteractionResponse(Interaction interaction);
}
