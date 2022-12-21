package com.example.adminservice.service;

import com.example.adminservice.dto.PartnerDTO;
import com.example.adminservice.dto.SearchPartnerDTO;
import com.example.adminservice.model.CustomUserDetails;
import com.example.adminservice.model.Partner;
import org.springframework.data.domain.Page;

public interface PartnerService {
    PartnerDTO createPartner(CustomUserDetails customUserDetails, PartnerDTO partnerDTO);

    Page<Partner> findAll(CustomUserDetails customUserDetails, SearchPartnerDTO searchPartnerDTO);

    PartnerDTO updatePartner(CustomUserDetails customUserDetails, PartnerDTO partnerDTO);

    void lockPartner(CustomUserDetails customUserDetails, String code);

    void unlockPartner(CustomUserDetails customUserDetails, String code);

    void deletePartner(CustomUserDetails customUserDetails, String code);
}
