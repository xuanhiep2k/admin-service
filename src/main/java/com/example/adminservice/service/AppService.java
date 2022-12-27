package com.example.adminservice.service;

import com.example.adminservice.dto.AppDTO;
import com.example.adminservice.dto.SearchAppDTO;
import com.example.adminservice.model.App;
import com.example.adminservice.model.CustomUserDetails;
import org.springframework.data.domain.Page;

public interface AppService {
    AppDTO updateApp(CustomUserDetails customUserDetails, AppDTO appDTO);

    Page<App> findAll(CustomUserDetails customUserDetails, SearchAppDTO searchAppDTO);

    AppDTO createApp(CustomUserDetails customUserDetails, AppDTO appDTO);

    void lockApp(CustomUserDetails customUserDetails, String code);

    void unlockApp(CustomUserDetails customUserDetails, String code);

    void deleteApp(CustomUserDetails customUserDetails, String code);
}
