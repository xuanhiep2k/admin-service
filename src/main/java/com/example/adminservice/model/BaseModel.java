package com.example.adminservice.model;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonStringType.class),
        @TypeDef(name = "json", typeClass = JsonBinaryType.class)
})
public class BaseModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String status;
    @Column(name = "created_by")
    protected String createdBy;
    @Column(name = "updated_by")
    protected String updatedBy;
    @Column(name = "created_date")
    protected Date createdDate;
    @Column(name = "updated_date")
    protected Date updatedDate;
}
