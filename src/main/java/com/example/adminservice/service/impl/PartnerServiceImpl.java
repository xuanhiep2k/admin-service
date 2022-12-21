package com.example.adminservice.service.impl;

import com.example.adminservice.config.ErrorCode;
import com.example.adminservice.dto.PartnerDTO;
import com.example.adminservice.dto.SearchPartnerDTO;
import com.example.adminservice.exception.ServerException;
import com.example.adminservice.model.CustomUserDetails;
import com.example.adminservice.model.Partner;
import com.example.adminservice.repository.PartnerRepository;
import com.example.adminservice.service.ActionLogService;
import com.example.adminservice.service.PartnerService;
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
public class PartnerServiceImpl implements PartnerService {
    private final PartnerRepository partnerRepository;
    private final ActionLogService actionLogService;

    @Override
    public PartnerDTO createPartner(CustomUserDetails customUserDetails, PartnerDTO partnerDTO) {
        Partner partner = partnerRepository.findByCode(partnerDTO.getCode());
        if (partner == null) {
            partner = new Partner();
            BeanUtils.copyProperties(partnerDTO, partner);
            partner.setCreatedBy(customUserDetails.getUsername());
            partner.setCreatedDate(new Date());
            partner.setStatus(Constants.STATUS.ACTIVE);
            partnerRepository.saveAndFlush(partner);
            actionLogService.createLog(customUserDetails, Constants.ACTION.CREATE, Constants.TITLE_LOG.PARTNER,
                    MessageFormat.format("Đã tạo đối tác {0} thành công!", partnerDTO.getName()));
        } else {
            if (!partner.getStatus().equals(Constants.STATUS.ACTIVE)) {
                throw new ServerException(ErrorCode.BAD_REQUEST, "Đối tác đang ở trạng thái khóa");
            } else {
                throw new ServerException(ErrorCode.BAD_REQUEST, "Đối tác đã tồn tại");
            }
        }
        return partnerDTO;
    }

    @Override
    public Page<Partner> findAll(CustomUserDetails customUserDetails, SearchPartnerDTO searchPartnerDTO) {
        Sort sort = Sort.by(searchPartnerDTO.getSortDirection(), searchPartnerDTO.getSortBy());
        Pageable pageable = PageRequest.of(searchPartnerDTO.getPageNumber(), searchPartnerDTO.getPageSize(), sort);
        Specification<Partner> specification = SpecificationFilter.specificationPartner(searchPartnerDTO);

        actionLogService.createLog(customUserDetails, Constants.ACTION.SEARCH, Constants.TITLE_LOG.PARTNER,
                MessageFormat.format("Đã tìm kiếm Partners", searchPartnerDTO.toString()));
        return partnerRepository.findAll(specification, pageable);
    }

    @Override
    public PartnerDTO updatePartner(CustomUserDetails customUserDetails, PartnerDTO partnerDTO) {
        Partner partner = partnerRepository.findByCode(partnerDTO.getCode());
        if (partner == null) {
            throw new ServerException(ErrorCode.BAD_REQUEST, "Đối tác không tồn tại");
        } else {
            partner.setName(partnerDTO.getName());
            partner.setDescription(partnerDTO.getDescription());
            partner.setSizeRole(partnerDTO.getSizeRole());
            partner.setUpdatedBy(customUserDetails.getUsername());
            partner.setUpdatedDate(new Date());
            partnerRepository.save(partner);
            actionLogService.createLog(customUserDetails, Constants.ACTION.UPDATE, Constants.TITLE_LOG.PARTNER,
                    MessageFormat.format("Đã cập nhật đối tác {0} thành công", partner.getCode()));
        }
        return partnerDTO;
    }

    @Override
    public void lockPartner(CustomUserDetails customUserDetails, String code) {
        Partner partner = partnerRepository.findByCode(code);
        if (partner == null) {
            throw new ServerException(ErrorCode.NOT_FOUND, MessageFormat.format("Partner {0} không tồn tại", code));
        } else if (!partner.getStatus().equals(Constants.STATUS.ACTIVE)) {
            throw new ServerException(ErrorCode.BAD_REQUEST, MessageFormat.format("Partner {0} đang bị khoá", code));
        } else {
            partner.setStatus(Constants.STATUS.LOCKED);
            partnerRepository.save(partner);
            actionLogService.createLog(customUserDetails, Constants.ACTION.UPDATE, Constants.TITLE_LOG.PARTNER,
                    MessageFormat.format("Đã khoá đối tác {0} thành công", code));
        }
    }

    @Override
    public void unlockPartner(CustomUserDetails customUserDetails, String code) {
        Partner partner = partnerRepository.findByCode(code);
        if (partner == null) {
            throw new ServerException(ErrorCode.NOT_FOUND, MessageFormat.format("Partner {0} không tồn tại", code));
        } else if (partner.getStatus().equals(Constants.STATUS.ACTIVE)) {
            throw new ServerException(ErrorCode.BAD_REQUEST, MessageFormat.format("Partner {0} chưa được khoá", code));
        } else {
            partner.setStatus(Constants.STATUS.ACTIVE);
            partnerRepository.save(partner);
            actionLogService.createLog(customUserDetails, Constants.ACTION.UPDATE, Constants.TITLE_LOG.PARTNER,
                    MessageFormat.format("Mở khoá đối tác {0} thành công", code));
        }
    }

    @Override
    public void deletePartner(CustomUserDetails customUserDetails, String code) {
        Partner partner = partnerRepository.findByCode(code);
        if (partner == null) {
            throw new ServerException(ErrorCode.NOT_FOUND, MessageFormat.format("Partner {0} không tồn tại", code));
        } else if (partner.getStatus().equals(Constants.STATUS.ACTIVE)) {
            throw new ServerException(ErrorCode.BAD_REQUEST, MessageFormat.format("Partner {0} phải được khoá trước", code));
        } else {
            partnerRepository.delete(partner);
            actionLogService.createLog(customUserDetails, Constants.ACTION.DELETE, Constants.TITLE_LOG.PARTNER,
                    MessageFormat.format("Xoá đối tác {0} thành công", code));
        }
    }
}
