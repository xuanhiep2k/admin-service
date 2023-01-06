package com.example.adminservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MenuDTO {
    private String text;
    private String path;
    private Long value;
    private String code;
    private String icon;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<MenuDTO> children;


    public MenuDTO(String text, String path, Long value, String code, String icon) {
        this.text = text;
        this.path = path;
        this.value = value;
        this.icon = icon;
        this.code = code;
        this.children = null;
    }

    public void addChild(MenuDTO treeDTO) {
        if (this.children == null) this.children = new ArrayList<>();
        this.children.add(treeDTO);
    }
}
