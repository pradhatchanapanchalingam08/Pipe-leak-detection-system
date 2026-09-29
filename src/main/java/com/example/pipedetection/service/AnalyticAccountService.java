package com.example.pipedetection.service;

import com.example.pipedetection.entity.AnalyticAccount;
import com.example.pipedetection.exception.ResourceNotFoundException;
import com.example.pipedetection.repository.AnalyticAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalyticAccountService {

    @Autowired
    private AnalyticAccountRepository analyticAccountRepository;

    public AnalyticAccount createAnalyticAccount(AnalyticAccount analyticAccount) {
        return analyticAccountRepository.save(analyticAccount);
    }

    public List<AnalyticAccount> getAllAnalyticAccounts() {
        return analyticAccountRepository.findAll();
    }

    public AnalyticAccount getAnalyticAccountById(Long id) {
        return analyticAccountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pipeline sector not found with id: " + id));
    }

    public AnalyticAccount updateAnalyticAccount(Long id, AnalyticAccount updated) {
        AnalyticAccount existing = getAnalyticAccountById(id);
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        return analyticAccountRepository.save(existing);
    }

    public void deleteAnalyticAccount(Long id) {
        AnalyticAccount existing = getAnalyticAccountById(id);
        analyticAccountRepository.delete(existing);
    }
}
