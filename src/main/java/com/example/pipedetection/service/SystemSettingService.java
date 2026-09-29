package com.example.pipedetection.service;

import com.example.pipedetection.entity.SystemSetting;
import com.example.pipedetection.repository.SystemSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemSettingService {

    @Autowired
    private SystemSettingRepository systemSettingRepository;

    /** Returns the single system settings row, creating it with defaults if it does not exist yet. */
    public SystemSetting getSettings() {
        return systemSettingRepository.findById(1L).orElseGet(() -> {
            SystemSetting settings = new SystemSetting();
            settings.setId(1L);
            return systemSettingRepository.save(settings);
        });
    }

    public SystemSetting updateLeakToleranceLimit(Double leakToleranceLimit) {
        SystemSetting settings = getSettings();
        settings.setLeakToleranceLimit(leakToleranceLimit);
        return systemSettingRepository.save(settings);
    }
}
