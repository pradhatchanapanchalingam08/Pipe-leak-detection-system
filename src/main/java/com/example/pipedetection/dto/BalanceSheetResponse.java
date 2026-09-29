package com.example.pipedetection.dto;

import java.math.BigDecimal;

/** Simple response object for the Balance Sheet report. */
public class BalanceSheetResponse {

    private BigDecimal pipeCrawlerDroneFleet;
    private BigDecimal sealantStock;
    private BigDecimal cashBank;
    private BigDecimal sealantSupplierPayables;
    private BigDecimal droneComponentCreditors;
    private BigDecimal totalAssets;
    private BigDecimal totalLiabilities;

    public BalanceSheetResponse(BigDecimal pipeCrawlerDroneFleet, BigDecimal sealantStock, BigDecimal cashBank,
                                 BigDecimal sealantSupplierPayables, BigDecimal droneComponentCreditors) {
        this.pipeCrawlerDroneFleet = pipeCrawlerDroneFleet;
        this.sealantStock = sealantStock;
        this.cashBank = cashBank;
        this.sealantSupplierPayables = sealantSupplierPayables;
        this.droneComponentCreditors = droneComponentCreditors;
        this.totalAssets = pipeCrawlerDroneFleet.add(sealantStock).add(cashBank);
        this.totalLiabilities = sealantSupplierPayables.add(droneComponentCreditors);
    }

    public BigDecimal getPipeCrawlerDroneFleet() {
        return pipeCrawlerDroneFleet;
    }

    public BigDecimal getSealantStock() {
        return sealantStock;
    }

    public BigDecimal getCashBank() {
        return cashBank;
    }

    public BigDecimal getSealantSupplierPayables() {
        return sealantSupplierPayables;
    }

    public BigDecimal getDroneComponentCreditors() {
        return droneComponentCreditors;
    }

    public BigDecimal getTotalAssets() {
        return totalAssets;
    }

    public BigDecimal getTotalLiabilities() {
        return totalLiabilities;
    }
}
