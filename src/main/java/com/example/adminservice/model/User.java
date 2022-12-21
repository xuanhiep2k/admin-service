package com.example.adminservice.model;

import com.example.adminservice.dto.UserDTO;
import com.vladmihalcea.hibernate.type.array.ListArrayType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@TypeDef(name = "list-array", typeClass = ListArrayType.class)
@Table(name = "users")
public class User extends BaseModel implements Serializable {
    @Column(name = "full_name")
    private String fullName;
    private String email;
    private String phone;
    private String username;
    private String password;
    @Column(name = "department_code")
    private String departmentCode;
    @Column(name = "partner_code")
    private String partnerCode;
    @Type(type = "list-array")
    @Column(name = "roles", columnDefinition = "text[]")
    private List<String> roles;

    public User(UserDTO userDTO) {
        BeanUtils.copyProperties(userDTO, this);
    }

    public User() {
        super();
    }
}
