package com.example.adminservice.repository;

import com.example.adminservice.model.Function;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FunctionRepository extends JpaRepository<Function, Long> {
    Function findAllByCode(String code);

    Function findAllByPath(String path);
}
