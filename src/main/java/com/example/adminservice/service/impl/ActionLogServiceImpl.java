package com.example.adminservice.service.impl;

import com.example.adminservice.model.ActionLog;
import com.example.adminservice.model.CustomUserDetails;
import com.example.adminservice.repository.ActionLogRepository;
import com.example.adminservice.service.ActionLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class ActionLogServiceImpl implements ActionLogService {
    private final ActionLogRepository actionLogRepository;

    @Override
    public void createLog(CustomUserDetails customUserDetails, String method, String title, String log) {
        ActionLog actionLog = ActionLog.builder()
                .method(method)
                .title(title)
                .log(log)
                .createdBy(customUserDetails.getUsername())
                .createdDate(new Date())
                .build();
        actionLogRepository.saveAndFlush(actionLog);
    }
}
