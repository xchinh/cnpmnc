package com.example.cnpmnc.repository;

import com.example.cnpmnc.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByUserIdOrderByTimestampDesc(Long userId);
    List<Activity> findByEntityTypeAndEntityIdOrderByTimestampDesc(String entityType, Long entityId);
    List<Activity> findByTimestampBetweenOrderByTimestampDesc(LocalDateTime startDate, LocalDateTime endDate);
}