package com.example.pipedetection.dto;

import java.math.BigDecimal;

/** One row of the Pipeline Sector Budget Report. */
public class BudgetReportRow {

    private Long budgetId;
    private String pipelineSector;
    private Integer budgetYear;
    private BigDecimal plannedBudget;
    private BigDecimal sealantPurchases;
    private BigDecimal droneChargingExpenses;
    private BigDecimal repairRevenue;
    private BigDecimal remainingBudget;

    public BudgetReportRow(Long budgetId, String pipelineSector, Integer budgetYear, BigDecimal plannedBudget,
                            BigDecimal sealantPurchases, BigDecimal droneChargingExpenses, BigDecimal repairRevenue) {
        this.budgetId = budgetId;
        this.pipelineSector = pipelineSector;
        this.budgetYear = budgetYear;
        this.plannedBudget = plannedBudget;
        this.sealantPurchases = sealantPurchases;
        this.droneChargingExpenses = droneChargingExpenses;
        this.repairRevenue = repairRevenue;
        this.remainingBudget = plannedBudget.subtract(sealantPurchases).subtract(droneChargingExpenses);
    }

    public Long getBudgetId() {
        return budgetId;
    }

    public String getPipelineSector() {
        return pipelineSector;
    }

    public Integer getBudgetYear() {
        return budgetYear;
    }

    public BigDecimal getPlannedBudget() {
        return plannedBudget;
    }

    public BigDecimal getSealantPurchases() {
        return sealantPurchases;
    }

    public BigDecimal getDroneChargingExpenses() {
        return droneChargingExpenses;
    }

    public BigDecimal getRepairRevenue() {
        return repairRevenue;
    }

    public BigDecimal getRemainingBudget() {
        return remainingBudget;
    }
}
