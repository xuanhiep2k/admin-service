package com.example.adminservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "role_function")
@AllArgsConstructor
public class RoleFunction extends BaseModel {
    @Column(name = "role_code")
    private String roleCode;
    @Column(name = "function_code")
    private String functionCode;
}
