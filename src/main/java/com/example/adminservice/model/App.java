package com.example.adminservice.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class App extends BaseModel {
    private String code;
    private String name;
    private String description;
}
