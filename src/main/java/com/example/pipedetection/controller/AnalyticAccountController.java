package com.example.pipedetection.controller;

import com.example.pipedetection.entity.AnalyticAccount;
import com.example.pipedetection.service.AnalyticAccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytic-accounts")
@CrossOrigin(origins = "*")
public class AnalyticAccountController {

    @Autowired
    private AnalyticAccountService analyticAccountService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AnalyticAccount createAnalyticAccount(@Valid @RequestBody AnalyticAccount analyticAccount) {
        return analyticAccountService.createAnalyticAccount(analyticAccount);
    }

    @GetMapping
    public List<AnalyticAccount> getAllAnalyticAccounts() {
        return analyticAccountService.getAllAnalyticAccounts();
    }

    @GetMapping("/{id}")
    public AnalyticAccount getAnalyticAccount(@PathVariable Long id) {
        return analyticAccountService.getAnalyticAccountById(id);
    }

    @PutMapping("/{id}")
    public AnalyticAccount updateAnalyticAccount(@PathVariable Long id, @Valid @RequestBody AnalyticAccount analyticAccount) {
        return analyticAccountService.updateAnalyticAccount(id, analyticAccount);
    }

    @DeleteMapping("/{id}")
    public void deleteAnalyticAccount(@PathVariable Long id) {
        analyticAccountService.deleteAnalyticAccount(id);
    }
}
