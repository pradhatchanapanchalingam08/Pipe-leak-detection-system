package com.example.pipedetection.controller;

import com.example.pipedetection.dto.SalesOrderDetailResponse;
import com.example.pipedetection.dto.SalesOrderRequest;
import com.example.pipedetection.entity.SalesOrder;
import com.example.pipedetection.service.SalesOrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales-orders")
@CrossOrigin(origins = "*")
public class SalesOrderController {

    @Autowired
    private SalesOrderService salesOrderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SalesOrder createSalesOrder(@Valid @RequestBody SalesOrderRequest request) {
        return salesOrderService.createSalesOrder(request);
    }

    @GetMapping
    public List<SalesOrder> getAllSalesOrders() {
        return salesOrderService.getAllSalesOrders();
    }

    @GetMapping("/{id}")
    public SalesOrderDetailResponse getSalesOrderDetail(@PathVariable Long id) {
        return salesOrderService.getSalesOrderDetail(id);
    }

    @PostMapping("/{id}/complete")
    public SalesOrder markCompleted(@PathVariable Long id) {
        return salesOrderService.markCompleted(id);
    }
}
