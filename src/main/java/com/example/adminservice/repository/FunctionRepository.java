package com.example.adminservice.repository;

import com.example.adminservice.model.Function;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FunctionRepository extends JpaRepository<Function, Long> {
    List<Function> findAllByStatusAndAppCodeAndType(String statusActive, String appCode, String type);

    @Query(value = "select distinct f.*\n"
            + "from role r,\n"
            + "     role_function rf,\n"
            + "     function f\n"
            + "where rf.status = ?2\n"
            + "  and f.status = ?2\n"
            + "  and r.status = ?2\n"
            + "  and f.app_code = ?3\n"
            + "  and rf.function_code = f.code\n"
            + "  and rf.role_code = r.code\n"
            + "  and r.code in ?1", nativeQuery = true)
    List<Function> getFunctionActiveByUser(List<String> role, String statusActive, String appCode);

    @Query(value =
            "select distinct f.*\n"
                    + "from role r,\n"
                    + "     role_function rf,\n"
                    + "     function f\n"
                    + "where rf.status = ?2\n"
                    + "  and f.status = ?2\n"
                    + "  and rf.function_code = f.code\n"
                    + "  and rf.role_code = r.code\n"
                    + "  and r.code = ?1 order by f.created_date asc",
            nativeQuery = true)
    List<Function> getFunctionActiveByRole(String role, String statusActive);

    Function findAllByCode(String code);

    Function findAllByPath(String path);

    List<Function> findAll(Specification<Function> specification);

    Page<Function> findAll(Specification<Function> specification, Pageable pageable);

    List<Function> findAllByParentCodeAndStatus(String parentCode, String status);
}
