package com.example.pipedetection.controller;

import com.example.pipedetection.entity.SystemSetting;
import com.example.pipedetection.service.SystemSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/settings")
@CrossOrigin(origins = "*")
public class SystemSettingController {

    @Autowired
    private SystemSettingService systemSettingService;

    @GetMapping
    public SystemSetting getSettings() {
        return systemSettingService.getSettings();
    }

    /** Admin function: set the leak tolerance limit (acoustic level threshold). */
    @PutMapping("/leak-tolerance")
    public SystemSetting updateLeakTolerance(@RequestBody Map<String, Double> body) {
        return systemSettingService.updateLeakToleranceLimit(body.get("leakToleranceLimit"));
    }
}
