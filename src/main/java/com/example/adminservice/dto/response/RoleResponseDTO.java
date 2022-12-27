package com.example.adminservice.dto.response;

import com.example.adminservice.dto.RoleFunctionDTO;
import com.example.adminservice.dto.TreeNodeDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RoleResponseDTO {
    private Long id;
    private String code;
    private String name;
    private String description;
    private String partnerCode;
    private String status;
    private List<TreeNodeDTO> functions;
    private List<String> functionNames;
    private List<String> functionCodes;

    public RoleResponseDTO(RoleFunctionDTO rf) {
        BeanUtils.copyProperties(rf, this);
    }
}
