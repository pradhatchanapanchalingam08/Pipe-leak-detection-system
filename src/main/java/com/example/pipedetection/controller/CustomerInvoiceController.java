package com.example.pipedetection.controller;

import com.example.pipedetection.dto.CreateInvoiceRequest;
import com.example.pipedetection.entity.CustomerInvoice;
import com.example.pipedetection.service.CustomerInvoiceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@CrossOrigin(origins = "*")
public class CustomerInvoiceController {

    @Autowired
    private CustomerInvoiceService customerInvoiceService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerInvoice createInvoice(@Valid @RequestBody CreateInvoiceRequest request) {
        return customerInvoiceService.createInvoiceFromSalesOrder(request.getSalesOrderId());
    }

    @GetMapping
    public List<CustomerInvoice> getAllInvoices() {
        return customerInvoiceService.getAllInvoices();
    }

    @GetMapping("/{id}")
    public CustomerInvoice getInvoice(@PathVariable Long id) {
        return customerInvoiceService.getInvoiceById(id);
    }

    @PostMapping("/{id}/mark-paid")
    public CustomerInvoice markPaid(@PathVariable Long id) {
        return customerInvoiceService.markPaid(id);
    }
}
