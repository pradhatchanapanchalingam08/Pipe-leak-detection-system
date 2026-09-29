package com.example.pipedetection.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/** Request body used when recording sealant (epoxy resin) consumption against a leak repair. */
public class SealantConsumptionRequest {

    @NotNull(message = "Leak id is required")
    private Long leakId;

    @NotNull(message = "Product id is required")
    private Long productId;

    @NotNull(message = "Quantity used is required")
    @Min(value = 1, message = "Quantity used must be at least 1")
    private Integer quantityUsed;

    public Long getLeakId() {
        return leakId;
    }

    public void setLeakId(Long leakId) {
        this.leakId = leakId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantityUsed() {
        return quantityUsed;
    }

    public void setQuantityUsed(Integer quantityUsed) {
        this.quantityUsed = quantityUsed;
    }
}
