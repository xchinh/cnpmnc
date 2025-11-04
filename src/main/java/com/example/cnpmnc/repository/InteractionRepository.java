package com.example.cnpmnc.repository;

import com.example.cnpmnc.entity.Interaction;
import com.example.cnpmnc.enums.InteractionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InteractionRepository extends JpaRepository<Interaction, Long> {
    
    @Query("SELECT i FROM Interaction i WHERE i.customerId = :customerId AND i.deletedAt IS NULL " +
           "AND (:type IS NULL OR i.type = :type) " +
           "AND (:startDate IS NULL OR i.interactionDate >= :startDate) " +
           "AND (:endDate IS NULL OR i.interactionDate <= :endDate) " +
           "ORDER BY i.interactionDate DESC")
    List<Interaction> findByCustomerIdWithFilters(
            @Param("customerId") Long customerId,
            @Param("type") InteractionType type,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
    
    @Query("SELECT COUNT(i) FROM Interaction i WHERE i.customerId = :customerId AND i.deletedAt IS NULL " +
           "AND (:type IS NULL OR i.type = :type) " +
           "AND (:startDate IS NULL OR i.interactionDate >= :startDate) " +
           "AND (:endDate IS NULL OR i.interactionDate <= :endDate)")
    Long countByCustomerIdWithFilters(
            @Param("customerId") Long customerId,
            @Param("type") InteractionType type,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
    
    Optional<Interaction> findByIdAndCustomerIdAndDeletedAtIsNull(Long id, Long customerId);
    
    List<Interaction> findByCustomerIdAndDeletedAtIsNullOrderByInteractionDateDesc(Long customerId);
}