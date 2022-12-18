package com.example.adminservice.service;

import com.example.adminservice.model.CustomUserDetails;

public interface ActionLogService {
    void createLog(CustomUserDetails customUserDetails, String method, String title, String log);
}
