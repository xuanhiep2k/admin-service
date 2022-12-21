package com.example.adminservice.repository;

import com.example.adminservice.model.RoleFunction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface RoleFunctionRepository extends JpaRepository<RoleFunction, Long> {
    @Query(value = "Select r.code " +
            "from role r, role_function rf, function f " +
            "where r.code = rf.role_code and f.code = rf.function_code " +
            "and r.status = ?2 and f.status = ?2 " +
            "and path = ?1", nativeQuery = true)
    List<String> getRoleByPath(String path, String status);

    List<RoleFunction> findAllByRoleCode(String roleCode);

}
