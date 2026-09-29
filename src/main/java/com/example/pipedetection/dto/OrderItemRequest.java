package com.example.pipedetection.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;

/** A single line item used when creating a Purchase Order or Sales Order. */
public class OrderItemRequest {

    @NotNull(message = "Product id is required for each item")
    private Long productId;

    @NotNull(message = "Quantity is required for each item")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    /** Optional. If not supplied, the product's current unit price is used. */
    private BigDecimal unitPrice;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}
