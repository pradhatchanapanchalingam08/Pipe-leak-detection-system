package com.example.pipedetection.dto;

import java.math.BigDecimal;

/** Simple response object for the Profit & Loss report. */
public class ProfitLossResponse {

    private BigDecimal pipeRepairRevenues;
    private BigDecimal preventiveInspectionFees;
    private BigDecimal sealantMaterialCosts;
    private BigDecimal droneBatteryChargingExpenses;
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal netProfit;

    public ProfitLossResponse(BigDecimal pipeRepairRevenues, BigDecimal preventiveInspectionFees,
                               BigDecimal sealantMaterialCosts, BigDecimal droneBatteryChargingExpenses) {
        this.pipeRepairRevenues = pipeRepairRevenues;
        this.preventiveInspectionFees = preventiveInspectionFees;
        this.sealantMaterialCosts = sealantMaterialCosts;
        this.droneBatteryChargingExpenses = droneBatteryChargingExpenses;
        this.totalIncome = pipeRepairRevenues.add(preventiveInspectionFees);
        this.totalExpenses = sealantMaterialCosts.add(droneBatteryChargingExpenses);
        this.netProfit = totalIncome.subtract(totalExpenses);
    }

    public BigDecimal getPipeRepairRevenues() {
        return pipeRepairRevenues;
    }

    public BigDecimal getPreventiveInspectionFees() {
        return preventiveInspectionFees;
    }

    public BigDecimal getSealantMaterialCosts() {
        return sealantMaterialCosts;
    }

    public BigDecimal getDroneBatteryChargingExpenses() {
        return droneBatteryChargingExpenses;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public BigDecimal getTotalExpenses() {
        return totalExpenses;
    }

    public BigDecimal getNetProfit() {
        return netProfit;
    }
}
