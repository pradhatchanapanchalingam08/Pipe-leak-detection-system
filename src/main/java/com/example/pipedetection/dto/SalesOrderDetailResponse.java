package com.example.pipedetection.dto;

import com.example.pipedetection.entity.SalesOrder;
import com.example.pipedetection.entity.SalesOrderItem;
import java.util.List;

/** Combines a sales order with its line items for the "view details" screen. */
public class SalesOrderDetailResponse {

    private SalesOrder salesOrder;
    private List<SalesOrderItem> items;

    public SalesOrderDetailResponse(SalesOrder salesOrder, List<SalesOrderItem> items) {
        this.salesOrder = salesOrder;
        this.items = items;
    }

    public SalesOrder getSalesOrder() {
        return salesOrder;
    }

    public void setSalesOrder(SalesOrder salesOrder) {
        this.salesOrder = salesOrder;
    }

    public List<SalesOrderItem> getItems() {
        return items;
    }

    public void setItems(List<SalesOrderItem> items) {
        this.items = items;
    }
}
