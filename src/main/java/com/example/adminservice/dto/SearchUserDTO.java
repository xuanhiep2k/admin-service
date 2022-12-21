package com.example.adminservice.dto;

import com.example.adminservice.model.PageModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchUserDTO extends PageModel {
    private String username;
    private String fullName;
    private String email;

    @Override
    public String toString() {
        return "SearchUserDTO{" +
                "username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
