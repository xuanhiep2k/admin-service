package com.example.adminservice.service.impl;

import com.example.adminservice.config.ErrorCode;
import com.example.adminservice.dto.AppDTO;
import com.example.adminservice.dto.PartnerDTO;
import com.example.adminservice.dto.SearchAppDTO;
import com.example.adminservice.exception.ServerException;
import com.example.adminservice.model.App;
import com.example.adminservice.model.CustomUserDetails;
import com.example.adminservice.model.Partner;
import com.example.adminservice.repository.AppRepository;
import com.example.adminservice.service.ActionLogService;
import com.example.adminservice.service.AppService;
import com.example.adminservice.utils.Constants;
import com.example.adminservice.utils.specifications.SpecificationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AppServiceImpl implements AppService {
    private final AppRepository appRepository;
    private final ActionLogService actionLogService;

    @Override
    public AppDTO createApp(CustomUserDetails customUserDetails, AppDTO appDTO) {
        App app = appRepository.findByCode(appDTO.getCode());
        if (app == null) {
            app = new App();
            BeanUtils.copyProperties(appDTO, app);
            app.setCreatedBy(customUserDetails.getUsername());
            app.setCreatedDate(new Date());
            app.setStatus(Constants.STATUS.ACTIVE);
            appRepository.saveAndFlush(app);
            actionLogService.createLog(customUserDetails, Constants.ACTION.CREATE, Constants.TITLE_LOG.APP,
                    MessageFormat.format("Đã tạo ứng dụng {0} thành công!", appDTO.getName()));
        } else {
            if (!app.getStatus().equals(Constants.STATUS.ACTIVE)) {
                throw new ServerException(ErrorCode.BAD_REQUEST, "Ứng dụng đang ở trạng thái khóa");
            } else {
                throw new ServerException(ErrorCode.BAD_REQUEST, "Ứng dụng đã tồn tại");
            }
        }
        return appDTO;
    }

    @Override
    public void lockApp(CustomUserDetails customUserDetails, String code) {
        App app = appRepository.findByCode(code);
        if (app == null) {
            throw new ServerException(ErrorCode.NOT_FOUND, MessageFormat.format("App {0} không tồn tại", code));
        } else if (!app.getStatus().equals(Constants.STATUS.ACTIVE)) {
            throw new ServerException(ErrorCode.BAD_REQUEST, MessageFormat.format("App {0} đang bị khoá", code));
        } else {
            app.setStatus(Constants.STATUS.LOCKED);
            appRepository.save(app);
            actionLogService.createLog(customUserDetails, Constants.ACTION.LOCK, Constants.TITLE_LOG.APP,
                    MessageFormat.format("Đã khoá ứng dụng {0} thành công", code));
        }
    }

    @Override
    public void unlockApp(CustomUserDetails customUserDetails, String code) {
        App app = appRepository.findByCode(code);
        if (app == null) {
            throw new ServerException(ErrorCode.NOT_FOUND, MessageFormat.format("App {0} không tồn tại", code));
        } else if (app.getStatus().equals(Constants.STATUS.ACTIVE)) {
            throw new ServerException(ErrorCode.BAD_REQUEST, MessageFormat.format("App {0} chưa được khoá", code));
        } else {
            app.setStatus(Constants.STATUS.ACTIVE);
            appRepository.save(app);
            actionLogService.createLog(customUserDetails, Constants.ACTION.UNLOCK, Constants.TITLE_LOG.APP,
                    MessageFormat.format("Mở khoá ứng dụng {0} thành công", code));
        }
    }

    @Override
    public void deleteApp(CustomUserDetails customUserDetails, String code) {
        App app = appRepository.findByCode(code);
        if (app == null) {
            throw new ServerException(ErrorCode.NOT_FOUND, MessageFormat.format("App {0} không tồn tại", code));
        } else if (app.getStatus().equals(Constants.STATUS.ACTIVE)) {
            throw new ServerException(ErrorCode.BAD_REQUEST, MessageFormat.format("App {0} phải được khoá trước", code));
        } else {
            appRepository.delete(app);
            actionLogService.createLog(customUserDetails, Constants.ACTION.DELETE, Constants.TITLE_LOG.APP,
                    MessageFormat.format("Xoá ứng dụng {0} thành công", code));
        }
    }

    @Override
    public AppDTO updateApp(CustomUserDetails customUserDetails, AppDTO appDTO) {
        App app = appRepository.findByCode(appDTO.getCode());
        if (app == null) {
            throw new ServerException(ErrorCode.BAD_REQUEST, "Ứng dụng không tồn tại");
        } else {
            BeanUtils.copyProperties(appDTO, app);
            app.setUpdatedBy(customUserDetails.getUsername());
            app.setUpdatedDate(new Date());
            appRepository.save(app);
            actionLogService.createLog(customUserDetails, Constants.ACTION.UPDATE, Constants.TITLE_LOG.APP,
                    MessageFormat.format("Đã cập nhật ứng dụng {0} thành công", app.getCode()));
        }
        return appDTO;
    }

    @Override
    public Page<App> findAll(CustomUserDetails customUserDetails, SearchAppDTO searchAppDTO) {
        Sort sort = Sort.by(searchAppDTO.getSortDirection(), searchAppDTO.getSortBy());
        Pageable pageable;
        if (searchAppDTO.getPageSize() == 0) {
            pageable = PageRequest.of(searchAppDTO.getPageNumber(), Integer.MAX_VALUE, sort);
        } else {
            pageable = PageRequest.of(searchAppDTO.getPageNumber(), searchAppDTO.getPageSize(), sort);
        }
        Specification<App> specification = SpecificationFilter.specificationApp(searchAppDTO);

        actionLogService.createLog(customUserDetails, Constants.ACTION.SEARCH, Constants.TITLE_LOG.APP,
                MessageFormat.format("Đã tìm kiếm App", searchAppDTO.toString()));
        return appRepository.findAll(specification, pageable);
    }
}
