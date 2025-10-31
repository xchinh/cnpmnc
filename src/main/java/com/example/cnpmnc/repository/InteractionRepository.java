package com.example.cnpmnc.repository;

import com.example.cnpmnc.entity.Interaction;
import com.example.cnpmnc.enums.InteractionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InteractionRepository extends JpaRepository<Interaction, Long> {
    List<Interaction> findByCustomerIdAndDeletedAtIsNull(Long customerId);
    List<Interaction> findByCustomerIdAndDeletedAtIsNullOrderByInteractionDateDesc(Long customerId);
    List<Interaction> findByCustomerIdAndTypeAndDeletedAtIsNull(Long customerId, InteractionType type);
    List<Interaction> findByCustomerIdAndInteractionDateBetweenAndDeletedAtIsNull(
            Long customerId, LocalDateTime startDate, LocalDateTime endDate);
    Optional<Interaction> findByIdAndDeletedAtIsNull(Long id);
}