package com.example.adminservice.repository;

import com.example.adminservice.model.App;
import com.example.adminservice.model.Partner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AppRepository extends JpaRepository<App, Long>, JpaSpecificationExecutor<App> {
    Page<App> findAll(Specification<App> specification, Pageable pageable);

    App findByCode(String code);
}
