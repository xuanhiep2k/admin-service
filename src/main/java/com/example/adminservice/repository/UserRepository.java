package com.example.adminservice.repository;

import com.example.adminservice.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findByUsernameIgnoreCaseAndStatus(String username, String status);

    User findByUsername(String username);

    Page<User> findAll(Specification<User> specification, Pageable pageable);
}
