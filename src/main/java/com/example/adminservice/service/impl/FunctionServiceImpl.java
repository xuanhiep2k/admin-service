package com.example.adminservice.service.impl;

import com.example.adminservice.config.ErrorCode;
import com.example.adminservice.dto.FunctionDTO;
import com.example.adminservice.dto.SearchFunctionDTO;
import com.example.adminservice.dto.TreeNodeDTO;
import com.example.adminservice.exception.ServerException;
import com.example.adminservice.model.CustomUserDetails;
import com.example.adminservice.model.Function;
import com.example.adminservice.model.Role;
import com.example.adminservice.repository.FunctionRepository;
import com.example.adminservice.service.ActionLogService;
import com.example.adminservice.service.FunctionService;
import com.example.adminservice.utils.Constants;
import com.example.adminservice.utils.DataUtil;
import com.example.adminservice.utils.specifications.SpecificationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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

    @Override
    public FunctionDTO updateFunction(CustomUserDetails customUserDetails, FunctionDTO functionDTO) {
        Function function = functionRepository.findAllByCode(functionDTO.getCode());
        if (function == null) {
            throw new ServerException(ErrorCode.NOT_FOUND, "Chức năng không tìm thấy");
        } else {
            Function functionPath = functionRepository.findAllByPath(functionDTO.getPath());
            if (functionPath != null && !Objects.equals(functionPath.getCode(), function.getCode())) {
                if (functionPath.getStatus().equals(Constants.STATUS.LOCKED)) {
                    throw new ServerException(ErrorCode.BAD_REQUEST, "Chức năng đang ở trạng thái khóa");
                } else {
                    throw new ServerException(ErrorCode.BAD_REQUEST, "Đường dẫn đã tồn tại");
                }
            } else {
                BeanUtils.copyProperties(functionDTO, function);
            }
            function.setUpdatedBy(customUserDetails.getUsername());
            function.setUpdatedDate(new Date());
            functionRepository.save(function);
            actionLogService.createLog(customUserDetails, Constants.ACTION.UPDATE, Constants.TITLE_LOG.FUNCTION,
                    "Đã cập nhật chức năng " + functionDTO.getCode());
            return functionDTO;
        }
    }

    @Override
    public Page<TreeNodeDTO> getTree(CustomUserDetails customUserDetails, SearchFunctionDTO searchFunctionDTO) {
        Specification<Function> specification = SpecificationFilter.specificationFunction(searchFunctionDTO);
        List<Function> result = functionRepository.findAll(specification);
        Sort sort = Sort.by(searchFunctionDTO.getSortDirection(), searchFunctionDTO.getSortBy());
        Pageable pageable;
        if (searchFunctionDTO.getPageSize() == 0) {
            pageable = PageRequest.of(searchFunctionDTO.getPageNumber(), Integer.MAX_VALUE, sort);
        } else {
            pageable = PageRequest.of(searchFunctionDTO.getPageNumber(), searchFunctionDTO.getPageSize(), sort);
        }
        List<TreeNodeDTO> treeNodeDTOList = DataUtil.buildTree(result);

        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), treeNodeDTOList.size());
        Page<TreeNodeDTO> page;
        if (start > treeNodeDTOList.size()) {
            page = new PageImpl<>(new ArrayList<>(), pageable, treeNodeDTOList.size());
        } else {
            page = new PageImpl<>(treeNodeDTOList.subList(start, end), pageable, treeNodeDTOList.size());
        }
        actionLogService.createLog(customUserDetails, Constants.ACTION.SEARCH, Constants.TITLE_LOG.FUNCTION,
                MessageFormat.format("Đã tạo cây chức năng {0} thành công", treeNodeDTOList.size()));
        return page;
    }

    @Override
    public Page<Function> findAll(CustomUserDetails customUserDetails, SearchFunctionDTO searchFunctionDTO) {
        Sort sort = Sort.by(searchFunctionDTO.getSortDirection(), searchFunctionDTO.getSortBy());
        Pageable pageable = PageRequest.of(searchFunctionDTO.getPageNumber(), searchFunctionDTO.getPageSize(), sort);
        Specification<Function> specification = SpecificationFilter.specificationFunction(searchFunctionDTO);

        actionLogService.createLog(customUserDetails, Constants.ACTION.SEARCH, Constants.TITLE_LOG.FUNCTION,
                MessageFormat.format("Đã tìm kiếm Function", searchFunctionDTO.toString()));
        return functionRepository.findAll(specification, pageable);
    }

    @Override
    public void lockFunction(CustomUserDetails customUserDetails, String code) {
        Function function = functionRepository.findAllByCode(code);
        if (function == null) {
            throw new ServerException(ErrorCode.NOT_FOUND, "Chức năng không tồn tại");
        } else if (!function.getStatus().equals(Constants.STATUS.LOCKED)) {
            List<Function> functions = functionRepository.findAllByParentCodeAndStatus(function.getCode(), Constants.STATUS.ACTIVE);
            if (functions != null && functions.size() > 0) {
                for (Function f : functions) {
                    f.setStatus(Constants.STATUS.LOCKED);
                    f.setUpdatedBy(customUserDetails.getUsername());
                    f.setUpdatedDate(new Date());
                    functionRepository.save(f);
                }
            }
            function.setStatus(Constants.STATUS.LOCKED);
            function.setUpdatedBy(customUserDetails.getUsername());
            function.setUpdatedDate(new Date());
            functionRepository.save(function);
            actionLogService.createLog(customUserDetails, Constants.ACTION.LOCK, Constants.TITLE_LOG.FUNCTION,
                    "Đã khóa chức năng " + function.getCode());
        } else {
            throw new ServerException(ErrorCode.BAD_REQUEST, "Chức năng đang ở trạng thái khóa");
        }
    }

    @Override
    public void unlockFunction(CustomUserDetails customUserDetails, String code) {
        Function function = functionRepository.findAllByCode(code);
        if (function == null) {
            throw new ServerException(ErrorCode.NOT_FOUND, "Chức năng không tồn tại");
        } else if (!function.getStatus().equals(Constants.STATUS.ACTIVE)) {
            Function functionParent = functionRepository.findAllByCode(function.getParentCode());
            if (functionParent != null && functionParent.getStatus().contains(Constants.STATUS.LOCKED)) {
                throw new ServerException(ErrorCode.BAD_REQUEST, "Chức năng cha đang ở trạng thái khóa");
            }
            List<Function> functions = functionRepository.findAllByParentCodeAndStatus(function.getCode(), Constants.STATUS.LOCKED);
            if (functions != null && functions.size() > 0) {
                for (Function f : functions) {
                    f.setStatus(Constants.STATUS.ACTIVE);
                    f.setUpdatedBy(customUserDetails.getUsername());
                    f.setUpdatedDate(new Date());
                    functionRepository.save(f);
                }
            }
            function.setStatus(Constants.STATUS.ACTIVE);
            function.setUpdatedBy(customUserDetails.getUsername());
            function.setUpdatedDate(new Date());
            functionRepository.save(function);
            actionLogService.createLog(customUserDetails, Constants.ACTION.UNLOCK, Constants.TITLE_LOG.FUNCTION,
                    "Đã mở khóa chức năng " + function.getCode());
        } else {
            throw new ServerException(ErrorCode.BAD_REQUEST, "Chức năng đang ở trạng thái hoạt động");
        }
    }

    @Override
    public void deleteFunction(CustomUserDetails customUserDetails, String code) {
        Function function = functionRepository.findAllByCode(code);
        if (function == null) {
            throw new ServerException(ErrorCode.NOT_FOUND, "Chức năng không tồn tại");
        } else if (function.getStatus().equals(Constants.STATUS.LOCKED)) {
            List<Function> functions = functionRepository.findAllByParentCodeAndStatus(function.getCode(), Constants.STATUS.ACTIVE);
            if (functions != null && functions.size() > 0) {
                throw new ServerException(ErrorCode.BAD_REQUEST, "Chức năng con chưa được khóa hoàn toàn");
            } else {
                functionRepository.delete(function);
                functionRepository.deleteAll(functions);
                actionLogService.createLog(customUserDetails, Constants.ACTION.DELETE, Constants.TITLE_LOG.FUNCTION,
                        "Đã xóa chức năng " + function.getCode());
            }
        } else {
            throw new ServerException(ErrorCode.BAD_REQUEST, "Chức năng phải ở trạng thái khóa để thực hiện xóa");
        }
    }

    @Override
    public List<Function> getFunctionByRoleCode(CustomUserDetails customUserDetails, String[] roles) {
        List<Function> functions = new ArrayList<>();
        for (String role : roles) {
            List<Function> functionList = functionRepository.getFunctionActiveByRole(role, Constants.STATUS.ACTIVE);
            for (Function function : functionList) {
                functions.add(function);
            }
        }
        return functions;
    }
}
