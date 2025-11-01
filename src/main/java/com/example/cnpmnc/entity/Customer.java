package com.example.cnpmnc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "customers", indexes = {
    @Index(name = "idx_customers_email", columnList = "email"),
    @Index(name = "idx_customers_company", columnList = "company"),
    @Index(name = "idx_customers_team_id", columnList = "team_id"),
    @Index(name = "idx_customers_created_by", columnList = "created_by"),
    @Index(name = "idx_customers_name", columnList = "name"),
    @Index(name = "idx_customers_deleted_at", columnList = "deleted_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String email;

    @Column(length = 50)
    private String phone;

    private String company;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "profile_picture", length = 500)
    private String profilePicture;

    @Column(name = "team_id", nullable = false)
    private Long teamId;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Version
    @Column(nullable = false)
    private Integer version = 0;
}