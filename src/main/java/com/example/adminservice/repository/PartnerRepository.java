package com.example.adminservice.repository;

import com.example.adminservice.model.Partner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartnerRepository extends JpaRepository<Partner, Long> {
    Partner getByCode(String code);
}
