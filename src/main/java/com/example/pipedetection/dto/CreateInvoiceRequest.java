package com.example.pipedetection.dto;

import jakarta.validation.constraints.NotNull;

/** Request body for creating a customer invoice from a completed sales order. */
public class CreateInvoiceRequest {

    @NotNull(message = "Sales order id is required")
    private Long salesOrderId;

    public Long getSalesOrderId() {
        return salesOrderId;
    }

    public void setSalesOrderId(Long salesOrderId) {
        this.salesOrderId = salesOrderId;
    }
}
