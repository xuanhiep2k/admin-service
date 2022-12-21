package com.example.adminservice.dto;

import com.example.adminservice.model.PageModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchPartnerDTO extends PageModel {
    private String code;
    private String name;
    private String status;

    @Override
    public String toString() {
        return "SearchPartnerDTO{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
