package com.example.adminservice.repository;

import com.example.adminservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameIgnoreCaseAndStatus(String username, String status);

    boolean existsByUsername(String username);
}
