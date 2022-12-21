package com.example.adminservice.service.impl;

import com.example.adminservice.config.ErrorCode;
import com.example.adminservice.dto.FunctionDTO;
import com.example.adminservice.exception.ServerException;
import com.example.adminservice.model.CustomUserDetails;
import com.example.adminservice.model.Function;
import com.example.adminservice.repository.FunctionRepository;
import com.example.adminservice.service.ActionLogService;
import com.example.adminservice.service.FunctionService;
import com.example.adminservice.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class FunctionServiceImpl implements FunctionService {
    private final FunctionRepository functionRepository;
    private final ActionLogService actionLogService;

    @Override
    public FunctionDTO createFunction(CustomUserDetails customUserDetails, FunctionDTO functionDTO) {
        Function function = functionRepository.findAllByCode(functionDTO.getCode());
        Function functionPath = functionRepository.findAllByPath(functionDTO.getPath());
        if (function == null && functionPath == null) {
            function = new Function();
            BeanUtils.copyProperties(functionDTO, function);
            function.setCreatedBy(customUserDetails.getUsername());
            function.setCreatedDate(new Date());
            function.setStatus(Constants.STATUS.ACTIVE);
            functionRepository.save(function);
            actionLogService.createLog(customUserDetails, Constants.ACTION.CREATE, Constants.TITLE_LOG.FUNCTION,
                    MessageFormat.format("Đã tạo chức năng {0} thành công", functionDTO.getName()));
        } else {
            if (functionPath != null) {
                throw new ServerException(ErrorCode.BAD_REQUEST, "Đường dẫn đã tồn tại");
            }
            if (!function.getStatus().equals(Constants.STATUS.ACTIVE)) {
                throw new ServerException(ErrorCode.BAD_REQUEST, "Chức năng đang ở trạng thái khóa");
            } else {
                throw new ServerException(ErrorCode.BAD_REQUEST,
                        MessageFormat.format("Chức năng {0} đã tồn tại", functionDTO.getCode()));
            }
        }
        return functionDTO;
    }
}
