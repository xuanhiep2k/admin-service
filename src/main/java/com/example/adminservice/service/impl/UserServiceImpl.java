package com.example.adminservice.service.impl;

import com.example.adminservice.config.ErrorCode;
import com.example.adminservice.dto.MenuDTO;
import com.example.adminservice.dto.SearchUserDTO;
import com.example.adminservice.dto.UserDTO;
import com.example.adminservice.dto.request.UserRequestDTO;
import com.example.adminservice.exception.ServerException;
import com.example.adminservice.model.*;
import com.example.adminservice.repository.RoleRepository;
import com.example.adminservice.repository.UserRepository;
import com.example.adminservice.service.ActionLogService;
import com.example.adminservice.service.DepartmentService;
import com.example.adminservice.service.RoleService;
import com.example.adminservice.service.UserService;
import com.example.adminservice.utils.Constants;
import com.example.adminservice.utils.DataUtil;
import com.example.adminservice.utils.specifications.SpecificationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final DepartmentService departmentService;
    private final RoleService roleService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ActionLogService actionLogService;
    @Value("${spring.servlet.multipart.location}")
    private String UPLOADED_FOLDER;

    @Override
    public UserDTO findByUsernameIgnoreCaseAndStatus(String username, String appCode) {
        User user = userRepository.findByUsernameIgnoreCaseAndStatus(username, Constants.STATUS.ACTIVE);
        if (user != null) {
            UserDTO userDTO = new UserDTO(user);
            userDTO.setMenu(getMenu(userDTO.getRoles(), appCode,
                    appCode + "|" + String.join("|", userDTO.getRoles())));
            userDTO.setAuthorities(getGrantedAuthorities(userDTO.getRoles()));
            return userDTO;
        }
        return null;
    }

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
    public UserDTO findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            UserDTO userDTO = new UserDTO(user);
            userDTO.setAuthorities(getGrantedAuthorities(userDTO.getRoles()));
            return userDTO;
        }
        return null;
    }

    @Override
    public UserDTO getCurrentUserInfo(CustomUserDetails customUserDetails) {
        User user = userRepository.findByUsernameIgnoreCaseAndStatus(customUserDetails.getUsername(), Constants.STATUS.ACTIVE);
        if (user != null) {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            userDTO.setPassword("");
            actionLogService.createLog(customUserDetails, Constants.ACTION.SEARCH, Constants.TITLE_LOG.USER,
                    "Xem thông tin tài khoản " + userDTO.getUsername());
            return userDTO;
        } else {
            throw new ServerException(ErrorCode.NOT_FOUND, "Tài khoản không tồn tại trên hệ thống hoặc đang bị khóa");
        }
    }

    @Override
    public UserDTO createUser(CustomUserDetails customUserDetails, UserRequestDTO userRequestDTO, MultipartFile multipartFile) throws IOException {
        if (userRequestDTO.getRoles().contains(Constants.ROLE.ADMIN)) {
            throw new ServerException(ErrorCode.FORBIDDEN, "Bạn không thể tạo người dùng có quyền ADMIN");
        }
        if (DataUtil.isNullOrEmpty(userRequestDTO.getPartnerCode())) {
            userRequestDTO.setPartnerCode(customUserDetails.getPartnerCode());
        }
        UserDTO userDTO = findByUsername(userRequestDTO.getUsername());
        if (userDTO == null) {
            Department department =
                    departmentService.getByCode(userRequestDTO.getDepartmentCode(), userRequestDTO.getPartnerCode());
            if (department == null) {
                throw new ServerException(ErrorCode.NOT_FOUND,
                        MessageFormat.format("Phòng/ban {0} không tồn tại", userRequestDTO.getDepartmentCode()));
            }

            userDTO = new UserDTO(userRequestDTO);
            userDTO.setAvatar(null);
            // Upload avatar
            if (!Objects.isNull(multipartFile)) {
                long yourMilliSeconds = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                Date resultDate = new Date(yourMilliSeconds);
                String fileName = sdf.format(resultDate) + "_" + multipartFile.getOriginalFilename();
                String uploadDir = UPLOADED_FOLDER;
                FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
                userDTO.setAvatar(fileName);
            }
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
    public UserDTO updateUser(CustomUserDetails customUserDetails, UserRequestDTO userRequestDTO, MultipartFile multipartFile) throws IOException {
        UserDTO userDTO = findByUsername(userRequestDTO.getUsername());
        if (userRequestDTO.getRoles().contains(Constants.ROLE.ADMIN) && !userDTO.getRoles().contains(Constants.ROLE.ADMIN)) {
            throw new ServerException(ErrorCode.FORBIDDEN, "Bạn không thể tạo người dùng có quyền ADMIN");
        }
        if (userDTO == null) {
            throw new ServerException(ErrorCode.NOT_FOUND,
                    MessageFormat.format("Tài khoản {0} không tồn tại trong hệ thống", userRequestDTO.getUsername()));
        }

//        if (!userDTO.getDepartmentPath().startsWith(customUserDetails.getDepartmentPath())) {
//            throw new ServerException(ErrorCode.FORBIDDEN,
//                    MessageFormat.format("Không có quyền với tài khoản {0}", userRequestDTO.getUsername()));
//        }
        if (!customUserDetails.getRoles().contains(Constants.ROLE.ADMIN)) {
            List<Role> list = roleRepository.findAllByPartnerCodeAndStatus(customUserDetails.getPartnerCode(), Constants.STATUS.ACTIVE);
            List<String> roleCode = list.stream().map(Role::getCode).collect(Collectors.toList());
            for (String code : userDTO.getRoles()) {
                if (!roleCode.contains(code)) {
                    throw new ServerException(ErrorCode.BAD_REQUEST,
                            MessageFormat.format("Không thể thêm quyền {0} vào tài khoản", code));
                }
            }
        }
        BeanUtils.copyProperties(userRequestDTO, userDTO);
        // Upload avatar
        if (!Objects.isNull(multipartFile)) {
            long yourMilliSeconds = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            Date resultDate = new Date(yourMilliSeconds);
            String fileName = sdf.format(resultDate) + "_" + multipartFile.getOriginalFilename();
            String uploadDir = UPLOADED_FOLDER;
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
            userDTO.setAvatar(fileName);
        }
        userDTO.setUpdatedBy(userRequestDTO.getUsername());
        userDTO.setUpdatedDate(new Date());
        userDTO.setStatus(Constants.STATUS.ACTIVE);
        User user = new User(userDTO);
        user.setPassword(userDTO.getPassword());
        userRepository.save(user);
        actionLogService.createLog(customUserDetails, Constants.ACTION.UPDATE, Constants.TITLE_LOG.USER,
                "Đã cập nhập thông tin tài khoản " + userDTO.getUsername());
        return userDTO;
    }

    @Override
    public Page<User> findAll(CustomUserDetails customUserDetails, SearchUserDTO searchUserDTO) {
        Sort sort = Sort.by(searchUserDTO.getSortDirection(), searchUserDTO.getSortBy());
        Pageable pageable = PageRequest.of(searchUserDTO.getPageNumber(), searchUserDTO.getPageSize(), sort);
        Specification<User> specification = SpecificationFilter.specificationUser(searchUserDTO);

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
                System.out.println(new CustomUserDetails(userDTO));
                return new CustomUserDetails(userDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new UsernameNotFoundException("Username not found exception");
        }
    }

    @Cacheable(value = Constants.CACHE.MENU_OF_ROLE, key = "#cacheKey")
    public MenuDTO getMenu(List<String> role, String appCode, String cacheKey) {
        MenuDTO menuDTO = new MenuDTO("Menu", null, null, null, null);
        List<Function> list =
                roleService.listFunctionByUser(role, appCode, Constants.FUNCTION_TYPE.FUNCTION.name());
        List<Function> listParent =
                list.stream().filter(f -> f.getParentCode() == null
                        && f.getType().equals(Constants.FUNCTION_TYPE.FUNCTION.name())).collect(Collectors.toList());
        if (listParent.size() > 0) {
            for (Function f : listParent) {
                MenuDTO child = new MenuDTO(f.getName(), f.getPath(), f.getId(), f.getCode(), f.getIcon());
                menuDTO.addChild(child);
                list.remove(f);
                buildMenu(child, list);
            }
        }
        return menuDTO;
    }

    private void buildMenu(MenuDTO menuDTO, List<Function> listF) {
        if (listF.size() > 0) {
            List<Function> listChild =
                    listF.stream().filter(f -> Objects.equals(menuDTO.getCode(), f.getCode())
                            && f.getType().equals(Constants.FUNCTION_TYPE.FUNCTION.name())).collect(Collectors.toList());
            if (listChild.size() > 0) {
                for (Function f : listChild) {
                    MenuDTO child = new MenuDTO(f.getName(), f.getPath(), f.getId(), f.getCode(), f.getIcon());
                    menuDTO.addChild(child);
                    listF.remove(f);
                    buildMenu(child, listF);
                }
            }
        }
    }
}
