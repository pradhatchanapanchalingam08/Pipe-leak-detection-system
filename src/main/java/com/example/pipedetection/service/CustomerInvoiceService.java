package com.example.pipedetection.service;

import com.example.pipedetection.entity.CustomerInvoice;
import com.example.pipedetection.entity.SalesOrder;
import com.example.pipedetection.exception.BusinessRuleException;
import com.example.pipedetection.exception.ResourceNotFoundException;
import com.example.pipedetection.repository.CustomerInvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CustomerInvoiceService {

    @Autowired
    private CustomerInvoiceRepository customerInvoiceRepository;

    @Autowired
    private SalesOrderService salesOrderService;

    @Autowired
    private JournalService journalService;

    /** Creates a customer invoice from a sales order that has been marked COMPLETED. */
    public CustomerInvoice createInvoiceFromSalesOrder(Long salesOrderId) {
        SalesOrder order = salesOrderService.getSalesOrderById(salesOrderId);

        if (order.getStatus() != SalesOrder.SOStatus.COMPLETED) {
            throw new BusinessRuleException("Sales order #" + salesOrderId + " must be COMPLETED before an invoice can be created");
        }

        CustomerInvoice invoice = new CustomerInvoice();
        invoice.setCustomerId(order.getCustomerId());
        invoice.setSalesOrderId(order.getId());
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setAmount(order.getTotalAmount());
        invoice.setStatus(CustomerInvoice.InvoiceStatus.UNPAID);
        CustomerInvoice saved = customerInvoiceRepository.save(invoice);

        journalService.recordCredit("Pipe Repair Revenues", saved.getAmount(),
                "Customer invoice #" + saved.getId() + " created from sales order #" + salesOrderId,
                "CUSTOMER_INVOICE", saved.getId());

        return saved;
    }

    public List<CustomerInvoice> getAllInvoices() {
        return customerInvoiceRepository.findAll();
    }

    public CustomerInvoice getInvoiceById(Long id) {
        return customerInvoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer invoice not found with id: " + id));
    }

    public CustomerInvoice markPaid(Long id) {
        CustomerInvoice invoice = getInvoiceById(id);
        if (invoice.getStatus() == CustomerInvoice.InvoiceStatus.PAID) {
            throw new BusinessRuleException("Customer invoice #" + id + " is already marked as PAID");
        }
        invoice.setStatus(CustomerInvoice.InvoiceStatus.PAID);
        return customerInvoiceRepository.save(invoice);
    }
}
