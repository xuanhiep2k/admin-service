package com.example.adminservice.service;

import com.example.adminservice.dto.PartnerDTO;
import com.example.adminservice.model.CustomUserDetails;

public interface PartnerService {
    void createPartner(CustomUserDetails customUserDetails, PartnerDTO partnerDTO);
}
