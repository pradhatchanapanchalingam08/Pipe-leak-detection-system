package com.example.pipedetection.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

/** Request used by a logged-in customer to place an order. The customer id is taken from the session. */
public class CustomerOrderRequest {
    @NotEmpty(message = "At least one item is required")
    @Valid
    private List<OrderItemRequest> items;

    public List<OrderItemRequest> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
    }
}
