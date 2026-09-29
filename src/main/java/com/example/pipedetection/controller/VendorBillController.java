package com.example.pipedetection.controller;

import com.example.pipedetection.dto.CreateVendorBillRequest;
import com.example.pipedetection.entity.VendorBill;
import com.example.pipedetection.service.VendorBillService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendor-bills")
@CrossOrigin(origins = "*")
public class VendorBillController {

    @Autowired
    private VendorBillService vendorBillService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VendorBill createVendorBill(@Valid @RequestBody CreateVendorBillRequest request) {
        return vendorBillService.createVendorBill(request);
    }

    @GetMapping
    public List<VendorBill> getAllVendorBills() {
        return vendorBillService.getAllVendorBills();
    }

    @GetMapping("/{id}")
    public VendorBill getVendorBill(@PathVariable Long id) {
        return vendorBillService.getVendorBillById(id);
    }

    @PostMapping("/{id}/mark-paid")
    public VendorBill markPaid(@PathVariable Long id) {
        return vendorBillService.markPaid(id);
    }
}
