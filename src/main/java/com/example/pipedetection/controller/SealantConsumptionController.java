package com.example.pipedetection.controller;

import com.example.pipedetection.dto.SealantConsumptionRequest;
import com.example.pipedetection.entity.SealantConsumption;
import com.example.pipedetection.service.SealantConsumptionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sealant-consumption")
@CrossOrigin(origins = "*")
public class SealantConsumptionController {

    @Autowired
    private SealantConsumptionService sealantConsumptionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SealantConsumption recordConsumption(@Valid @RequestBody SealantConsumptionRequest request) {
        return sealantConsumptionService.recordConsumption(
                request.getLeakId(), request.getProductId(), request.getQuantityUsed());
    }

    @GetMapping
    public List<SealantConsumption> getAllConsumption() {
        return sealantConsumptionService.getAllConsumption();
    }

    @GetMapping("/leak/{leakId}")
    public List<SealantConsumption> getConsumptionForLeak(@PathVariable Long leakId) {
        return sealantConsumptionService.getConsumptionForLeak(leakId);
    }
}
