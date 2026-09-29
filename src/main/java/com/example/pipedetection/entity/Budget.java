package com.example.pipedetection.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "budgets")
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Analytic account id (pipeline sector) is required")
    @Column(nullable = false)
    private Long analyticAccountId;

    @NotNull(message = "Budget year is required")
    @Column(nullable = false)
    private Integer budgetYear;

    @Column(nullable = false)
    private BigDecimal plannedAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal sealantPurchaseAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal droneChargingAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal repairRevenueAmount = BigDecimal.ZERO;

    public Budget() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAnalyticAccountId() {
        return analyticAccountId;
    }

    public void setAnalyticAccountId(Long analyticAccountId) {
        this.analyticAccountId = analyticAccountId;
    }

    public Integer getBudgetYear() {
        return budgetYear;
    }

    public void setBudgetYear(Integer budgetYear) {
        this.budgetYear = budgetYear;
    }

    public BigDecimal getPlannedAmount() {
        return plannedAmount;
    }

    public void setPlannedAmount(BigDecimal plannedAmount) {
        this.plannedAmount = plannedAmount;
    }

    public BigDecimal getSealantPurchaseAmount() {
        return sealantPurchaseAmount;
    }

    public void setSealantPurchaseAmount(BigDecimal sealantPurchaseAmount) {
        this.sealantPurchaseAmount = sealantPurchaseAmount;
    }

    public BigDecimal getDroneChargingAmount() {
        return droneChargingAmount;
    }

    public void setDroneChargingAmount(BigDecimal droneChargingAmount) {
        this.droneChargingAmount = droneChargingAmount;
    }

    public BigDecimal getRepairRevenueAmount() {
        return repairRevenueAmount;
    }

    public void setRepairRevenueAmount(BigDecimal repairRevenueAmount) {
        this.repairRevenueAmount = repairRevenueAmount;
    }
}
