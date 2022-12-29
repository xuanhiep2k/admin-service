package com.example.adminservice.dto;

import com.example.adminservice.dto.request.UserRequestDTO;
import com.example.adminservice.model.User;
import lombok.*;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private String fullName;
    private String phone;
    private String username;
    private String password;
    private List<String> roles;
    private String status;
    private String avatar;
    private String accessToken;
    private Date createdDate;
    private Date updatedDate;
    private String createdBy;
    private String updatedBy;
    private String departmentPath;
    private String departmentCode;
    private String partnerCode;
    private List<GrantedAuthority> authorities;

    public UserDTO(User user) {
        this.setAuthorities(this.authorities);
        BeanUtils.copyProperties(user, this);
    }

    public UserDTO(UserRequestDTO userRequestDTO) {
        BeanUtils.copyProperties(userRequestDTO, this);
    }

    public UserDTO(User user, boolean ignorePassword) {
        BeanUtils.copyProperties(user, this);
        if (ignorePassword) {
            this.setPassword(null);
        }
    }
}
