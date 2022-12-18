package com.example.adminservice.service.impl;

import com.example.adminservice.config.ErrorCode;
import com.example.adminservice.dto.PartnerDTO;
import com.example.adminservice.exception.ServerException;
import com.example.adminservice.model.CustomUserDetails;
import com.example.adminservice.model.Partner;
import com.example.adminservice.repository.PartnerRepository;
import com.example.adminservice.service.ActionLogService;
import com.example.adminservice.service.PartnerService;
import com.example.adminservice.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class PartnerServiceImpl implements PartnerService {
    private final PartnerRepository partnerRepository;
    private final ActionLogService actionLogService;

    @Override
    public void createPartner(CustomUserDetails customUserDetails, PartnerDTO partnerDTO) {
        Partner partner = partnerRepository.getByCode(partnerDTO.getCode());
        if (partner == null) {
            BeanUtils.copyProperties(partnerDTO, partner);
            partner.setCreatedBy(customUserDetails.getUsername());
            partner.setCreatedDate(new Date());
            partner.setStatus(Constants.STATUS.ACTIVE);
            partnerRepository.saveAndFlush(partner);
            actionLogService.createLog(customUserDetails, Constants.ACTION.CREATE, Constants.TITLE_LOG.PARTNER,
                    MessageFormat.format("Đã tạo đối tác {0} thành công!",partnerDTO.getName()));
        } else {
            if (!partner.getStatus().equals(Constants.STATUS.ACTIVE)) {
                throw new ServerException(ErrorCode.BAD_REQUEST, "Chức năng đang ở trạng thái khóa");
            } else {
                throw new ServerException(ErrorCode.BAD_REQUEST, "Chức năng đã tồn tại");
            }
        }
    }
}
