package com.example.adminservice.dto;

import com.example.adminservice.model.Function;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TreeNodeDTO {
    Object data;
    Object value;
    Object key;
    Object title;
    List<Object> children = new ArrayList<>();

    public TreeNodeDTO(Object data) {
        this.data = data;
    }

    public TreeNodeDTO() {
    }

    public void addChildren(TreeNodeDTO treeTableDTO) {
        children.add(treeTableDTO);
    }

}
