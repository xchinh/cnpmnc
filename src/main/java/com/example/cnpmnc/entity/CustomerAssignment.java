package com.example.cnpmnc.entity;

import com.example.cnpmnc.enums.AssignmentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "customer_assignments",
    uniqueConstraints = @UniqueConstraint(name = "uk_customer_user_type",
        columnNames = {"customer_id", "user_id", "assignment_type"}),
    indexes = {
        @Index(name = "idx_cust_assign_customer_user", columnList = "customer_id, user_id"),
        @Index(name = "idx_cust_assign_customer_type", columnList = "customer_id, assignment_type"),
        @Index(name = "idx_cust_assign_user_id", columnList = "user_id")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "assignment_type", nullable = false)
    private AssignmentType assignmentType;

    @Column(name = "assigned_by")
    private Long assignedBy;

    @CreationTimestamp
    @Column(name = "assigned_at", nullable = false, updatable = false)
    private LocalDateTime assignedAt;
}