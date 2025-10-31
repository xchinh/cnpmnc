package com.example.cnpmnc.repository;

import com.example.cnpmnc.entity.CustomerAssignment;
import com.example.cnpmnc.enums.AssignmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerAssignmentRepository extends JpaRepository<CustomerAssignment, Long> {
    List<CustomerAssignment> findByCustomerId(Long customerId);
    List<CustomerAssignment> findByUserId(Long userId);
    List<CustomerAssignment> findByCustomerIdAndAssignmentType(Long customerId, AssignmentType assignmentType);
    Optional<CustomerAssignment> findByCustomerIdAndUserIdAndAssignmentType(Long customerId, Long userId, AssignmentType assignmentType);
}