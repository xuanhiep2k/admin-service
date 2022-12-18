package com.example.adminservice.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class Partner extends BaseModel {
    private String code;
    private String name;
    private String description;
    @ColumnDefault("10")
    @Column(name = "size_role")
    private Integer sizeRole;
}
