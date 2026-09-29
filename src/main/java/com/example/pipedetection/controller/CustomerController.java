package com.example.pipedetection.controller;

import com.example.pipedetection.entity.AppUser;
import com.example.pipedetection.entity.CustomerInvoice;
import com.example.pipedetection.entity.SalesOrder;
import com.example.pipedetection.entity.Product;
import com.example.pipedetection.dto.CustomerOrderRequest;
import com.example.pipedetection.dto.SalesOrderRequest;
import com.example.pipedetection.repository.ProductRepository;
import com.example.pipedetection.service.SalesOrderService;
import com.example.pipedetection.repository.CustomerInvoiceRepository;
import com.example.pipedetection.repository.SalesOrderRepository;
import com.example.pipedetection.service.CustomerInvoiceService;
import com.example.pipedetection.security.AuthSupport;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    private final SalesOrderRepository salesOrderRepository;
    private final CustomerInvoiceRepository invoiceRepository;
    private final CustomerInvoiceService invoiceService;
    private final ProductRepository productRepository;
    private final SalesOrderService salesOrderService;

    public CustomerController(SalesOrderRepository salesOrderRepository,
                              CustomerInvoiceRepository invoiceRepository,
                              CustomerInvoiceService invoiceService,
                              ProductRepository productRepository,
                              SalesOrderService salesOrderService) {
        this.salesOrderRepository = salesOrderRepository;
        this.invoiceRepository = invoiceRepository;
        this.invoiceService = invoiceService;
        this.productRepository = productRepository;
        this.salesOrderService = salesOrderService;
    }

    @GetMapping("/orders")
    public List<SalesOrder> orders(HttpSession session) {
        return salesOrderRepository.findByCustomerId(customerId(session));
    }

    @GetMapping("/invoices")
    public List<CustomerInvoice> invoices(HttpSession session) {
        return invoiceRepository.findByCustomerId(customerId(session));
    }

    @GetMapping("/products")
    public List<Product> products(HttpSession session) {
        customerId(session);
        return productRepository.findAll();
    }

    @PostMapping("/orders")
    public SalesOrder placeOrder(@Valid @RequestBody CustomerOrderRequest request, HttpSession session) {
        Long customerId = customerId(session);
        SalesOrderRequest orderRequest = new SalesOrderRequest();
        orderRequest.setCustomerId(customerId);
        // Never trust a price supplied by the browser. The service will use the
        // current database price when unitPrice is null.
        request.getItems().forEach(item -> item.setUnitPrice(null));
        orderRequest.setItems(request.getItems());
        return salesOrderService.createSalesOrder(orderRequest);
    }

    @PostMapping("/invoices/{id}/mark-paid")
    public CustomerInvoice markPaid(@PathVariable Long id, HttpSession session) {
        Long customerId = customerId(session);
        CustomerInvoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));
        if (!customerId.equals(invoice.getCustomerId())) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN, "This invoice does not belong to you");
        }
        return invoiceService.markPaid(id);
    }

    private Long customerId(HttpSession session) {
        AppUser user = AuthSupport.currentUser(session);
        if (user == null || user.getRole() != AppUser.Role.CUSTOMER || user.getCustomerId() == null) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN, "Customer account required");
        }
        return user.getCustomerId();
    }
}
