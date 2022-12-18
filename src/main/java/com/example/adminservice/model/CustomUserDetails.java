package com.example.adminservice.model;

import com.example.adminservice.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class CustomUserDetails extends User {
    private Long id;
    private List<String> roles;
    private String fullName;
    private String email;
    private String departmentCode;
    private String partnerCode;
    private String departmentPath;

    public CustomUserDetails(String username,
                             String password,
                             Collection<? extends GrantedAuthority> authorities,
                             Long id,
                             List<String> roles,
                             String fullName,
                             String email,
                             String departmentCode,
                             String partnerCode,
                             String departmentPath) {
        super(username, password, authorities);
        this.id = id;
        this.fullName = fullName;
        this.roles = roles;
        this.email = email;
        this.departmentCode = departmentCode;
        this.departmentPath = departmentPath;
        this.partnerCode = partnerCode;
    }

    public CustomUserDetails(UserDTO userDTO) {
        super(userDTO.getUsername(),
                userDTO.getPassword(),
                userDTO.getAuthorities());
        BeanUtils.copyProperties(userDTO, this);
    }
}
