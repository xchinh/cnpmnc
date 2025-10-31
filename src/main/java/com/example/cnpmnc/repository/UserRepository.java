package com.example.cnpmnc.repository;

import com.example.cnpmnc.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByIdAndDeletedAtIsNull(Long id);
    boolean existsByEmail(String email);
}