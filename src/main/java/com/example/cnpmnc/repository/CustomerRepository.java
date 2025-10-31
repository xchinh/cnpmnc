package com.example.cnpmnc.repository;

import com.example.cnpmnc.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByDeletedAtIsNull();
    Optional<Customer> findByIdAndDeletedAtIsNull(Long id);
    List<Customer> findByTeamIdAndDeletedAtIsNull(Long teamId);

    @Query("SELECT c FROM Customer c WHERE c.deletedAt IS NULL " +
            "AND (LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.company) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Customer> searchByKeyword(@Param("keyword") String keyword);
}