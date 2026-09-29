package com.example.pipedetection.service;

import com.example.pipedetection.entity.Budget;
import com.example.pipedetection.exception.ResourceNotFoundException;
import com.example.pipedetection.repository.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    public Budget createBudget(Budget budget) {
        return budgetRepository.save(budget);
    }

    public List<Budget> getAllBudgets() {
        return budgetRepository.findAll();
    }

    public Budget getBudgetById(Long id) {
        return budgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found with id: " + id));
    }

    public Budget updateBudget(Long id, Budget updated) {
        Budget existing = getBudgetById(id);
        existing.setAnalyticAccountId(updated.getAnalyticAccountId());
        existing.setBudgetYear(updated.getBudgetYear());
        existing.setPlannedAmount(updated.getPlannedAmount());
        existing.setSealantPurchaseAmount(updated.getSealantPurchaseAmount());
        existing.setDroneChargingAmount(updated.getDroneChargingAmount());
        existing.setRepairRevenueAmount(updated.getRepairRevenueAmount());
        return budgetRepository.save(existing);
    }

    public void deleteBudget(Long id) {
        Budget existing = getBudgetById(id);
        budgetRepository.delete(existing);
    }
}
