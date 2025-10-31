package com.example.cnpmnc.repository;

import com.example.cnpmnc.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findByDeletedAtIsNull();
    Optional<Team> findByIdAndDeletedAtIsNull(Long id);
}