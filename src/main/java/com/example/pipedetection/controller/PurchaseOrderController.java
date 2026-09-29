package com.example.pipedetection.controller;

import com.example.pipedetection.dto.PurchaseOrderDetailResponse;
import com.example.pipedetection.dto.PurchaseOrderRequest;
import com.example.pipedetection.entity.PurchaseOrder;
import com.example.pipedetection.service.PurchaseOrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-orders")
@CrossOrigin(origins = "*")
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PurchaseOrder createPurchaseOrder(@Valid @RequestBody PurchaseOrderRequest request) {
        return purchaseOrderService.createPurchaseOrder(request);
    }

    @GetMapping
    public List<PurchaseOrder> getAllPurchaseOrders() {
        return purchaseOrderService.getAllPurchaseOrders();
    }

    @GetMapping("/{id}")
    public PurchaseOrderDetailResponse getPurchaseOrderDetail(@PathVariable Long id) {
        return purchaseOrderService.getPurchaseOrderDetail(id);
    }

    @PostMapping("/{id}/confirm")
    public PurchaseOrder confirmPurchaseOrder(@PathVariable Long id) {
        return purchaseOrderService.updateStatus(id, PurchaseOrder.POStatus.CONFIRMED);
    }

    @PostMapping("/{id}/complete")
    public PurchaseOrder completePurchaseOrder(@PathVariable Long id) {
        return purchaseOrderService.updateStatus(id, PurchaseOrder.POStatus.COMPLETED);
    }
}
