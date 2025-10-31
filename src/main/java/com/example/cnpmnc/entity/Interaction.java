package com.example.cnpmnc.entity;

import com.example.cnpmnc.enums.InteractionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "interactions", indexes = {
    @Index(name = "idx_interactions_customer_id", columnList = "customer_id"),
    @Index(name = "idx_interactions_user_id", columnList = "user_id"),
    @Index(name = "idx_interactions_type", columnList = "type"),
    @Index(name = "idx_interactions_date", columnList = "interaction_date"),
    @Index(name = "idx_interactions_cust_date", columnList = "customer_id, interaction_date")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Interaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InteractionType type;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "interaction_date", nullable = false)
    private LocalDateTime interactionDate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}