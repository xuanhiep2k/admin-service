package com.example.adminservice.service.impl;

import com.example.adminservice.config.ErrorCode;
import com.example.adminservice.dto.SearchUserDTO;
import com.example.adminservice.dto.UserDTO;
import com.example.adminservice.dto.request.UserRequestDTO;
import com.example.adminservice.exception.ServerException;
import com.example.adminservice.model.*;
import com.example.adminservice.repository.UserRepository;
import com.example.adminservice.service.ActionLogService;
import com.example.adminservice.service.DepartmentService;
import com.example.adminservice.service.UserService;
import com.example.adminservice.utils.Constants;
import com.example.adminservice.utils.DataUtil;
import com.example.adminservice.utils.specifications.SpecificationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final DepartmentService departmentService;
    private final PasswordEncoder passwordEncoder;
    private final ActionLogService actionLogService;
    private SpecificationFilter specificationFilter;

    @Override
    public UserDTO findByUsernameIgnoreCaseAndStatus(String username) {
        User user = userRepository.findByUsernameIgnoreCaseAndStatus(username, Constants.STATUS.ACTIVE);
        if (user != null) {
            UserDTO userDTO = new UserDTO(user);
            userDTO.setAuthorities(getGrantedAuthorities(userDTO.getRoles()));
            return userDTO;
        }
        return null;
    }

    @Override
    public UserDTO createUser(CustomUserDetails customUserDetails, UserRequestDTO userRequestDTO) {
        if (userRequestDTO.getRoles().contains(Constants.ROLE.ADMIN)) {
            throw new ServerException(ErrorCode.FORBIDDEN, "Bạn không thể tạo username ADMIN");
        }
        if (DataUtil.isNullOrEmpty(userRequestDTO.getPartnerCode())) {
            userRequestDTO.setPartnerCode(customUserDetails.getPartnerCode());
        }
        UserDTO userDTO = findByUsernameIgnoreCaseAndStatus(userRequestDTO.getUsername());
        if (userDTO == null) {
            Department department =
                    departmentService.getByCode(userRequestDTO.getDepartmentCode(), userRequestDTO.getPartnerCode());
            if (department == null) {
                throw new ServerException(ErrorCode.NOT_FOUND,
                        MessageFormat.format("Phòng/ban {0} không tồn tại", userRequestDTO.getDepartmentCode()));
            }
            userDTO = new UserDTO(userRequestDTO);
            userDTO.setStatus(Constants.STATUS.ACTIVE);
            userDTO.setRoles(userRequestDTO.getRoles());
            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            userDTO.setCreatedBy(customUserDetails.getUsername());
            userDTO.setCreatedDate(new Date());
            userDTO.setUpdatedDate(new Date());
            userDTO.setDepartmentCode(department.getCode());
            userDTO.setDepartmentPath(department.getPath());
            userDTO.setPartnerCode(department.getPartnerCode());
            User user = new User(userDTO);
            userRepository.saveAndFlush(user);
            actionLogService.createLog(customUserDetails, Constants.ACTION.CREATE, Constants.TITLE_LOG.USER,
                    MessageFormat.format("Đã tạo User {0} thành công", userDTO.getUsername()));
        } else if (Objects.equals(userDTO.getStatus(), Constants.STATUS.ACTIVE)) {
            throw new ServerException(ErrorCode.BAD_REQUEST,
                    MessageFormat.format("Tài khoản {0} đã tồn tại trên hệ thống", userDTO.getUsername()));
        } else {
            throw new ServerException(ErrorCode.BAD_REQUEST,
                    MessageFormat.format("Tài khoản {0} đang khóa. Vui lòng mở lại để sử dụng", userDTO.getUsername()));
        }
        return userDTO;
    }

    @Override
    public Page<User> findAll(CustomUserDetails customUserDetails, SearchUserDTO searchUserDTO) {
        Sort sort = Sort.by(searchUserDTO.getSortDirection(), searchUserDTO.getSortBy());
        Pageable pageable = PageRequest.of(searchUserDTO.getPageNumber(), searchUserDTO.getPageSize(), sort);
        Specification<User> specification = specificationFilter.specificationUser(searchUserDTO);

        actionLogService.createLog(customUserDetails, Constants.ACTION.SEARCH, Constants.TITLE_LOG.USER,
                MessageFormat.format("Đã tìm kiếm Users", searchUserDTO.toString()));
        return userRepository.findAll(specification, pageable);
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (!DataUtil.isNullObject(roles)) {
            roles.forEach(
                    s -> authorities.add(new SimpleGrantedAuthority(s)));
        }
        return authorities;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        try {
            UserDTO userDTO = findByUsernameIgnoreCaseAndStatus(s);
            if (userDTO == null) {
                throw new UsernameNotFoundException("Tài khoản không tồn tại hoặc bị khóa");
            } else {
                return new CustomUserDetails(userDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new UsernameNotFoundException("Username not found exception");
        }
    }
}
