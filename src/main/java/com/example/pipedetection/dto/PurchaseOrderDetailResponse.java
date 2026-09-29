package com.example.pipedetection.dto;

import com.example.pipedetection.entity.PurchaseOrder;
import com.example.pipedetection.entity.PurchaseOrderItem;
import java.util.List;

/** Combines a purchase order with its line items for the "view details" screen. */
public class PurchaseOrderDetailResponse {

    private PurchaseOrder purchaseOrder;
    private List<PurchaseOrderItem> items;

    public PurchaseOrderDetailResponse(PurchaseOrder purchaseOrder, List<PurchaseOrderItem> items) {
        this.purchaseOrder = purchaseOrder;
        this.items = items;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public List<PurchaseOrderItem> getItems() {
        return items;
    }

    public void setItems(List<PurchaseOrderItem> items) {
        this.items = items;
    }
}
