package com.example.cnpmnc.repository;

import com.example.cnpmnc.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByCustomerIdAndDeletedAtIsNullOrderByCreatedAtDesc(Long customerId);
    Optional<Note> findByIdAndDeletedAtIsNull(Long id);
}