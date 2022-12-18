package com.example.adminservice.repository;

import com.example.adminservice.model.ActionLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionLogRepository extends JpaRepository<ActionLog, Long> {
}
