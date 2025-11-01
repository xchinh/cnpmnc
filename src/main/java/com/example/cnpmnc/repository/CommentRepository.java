package com.example.cnpmnc.repository;

import com.example.cnpmnc.entity.Comment;
import com.example.cnpmnc.enums.EntityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByEntityTypeAndEntityIdAndDeletedAtIsNullOrderByCreatedAtAsc(EntityType entityType, Long entityId);
    List<Comment> findByParentIdAndDeletedAtIsNull(Long parentId);
    Optional<Comment> findByIdAndDeletedAtIsNull(Long id);
}