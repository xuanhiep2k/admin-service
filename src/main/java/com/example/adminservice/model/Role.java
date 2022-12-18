package com.example.adminservice.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class Role extends BaseModel {
    private String code;
    private String name;
    @Column(name = "partner_code")
    private String partnerCode;
    private String description;
}
