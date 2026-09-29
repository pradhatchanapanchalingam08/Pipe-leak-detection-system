package com.example.pipedetection.service;

import com.example.pipedetection.entity.CustomerInvoice;
import com.example.pipedetection.entity.Payment;
import com.example.pipedetection.entity.VendorBill;
import com.example.pipedetection.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CustomerInvoiceService customerInvoiceService;

    @Autowired
    private VendorBillService vendorBillService;

    @Autowired
    private JournalService journalService;

    /**
     * Records a payment (customer or vendor). Updates the related invoice/bill status
     * to PAID and writes a simple journal entry for the cash movement.
     */
    public Payment recordPayment(Payment payment) {
        payment.setPaymentDate(payment.getPaymentDate() != null ? payment.getPaymentDate() : LocalDate.now());
        Payment saved = paymentRepository.save(payment);

        if (payment.getPaymentType() == Payment.PaymentType.CUSTOMER_PAYMENT) {
            CustomerInvoice invoice = customerInvoiceService.markPaid(payment.getReferenceId());
            journalService.recordDebit("Cash/Bank", saved.getAmount(),
                    "Customer payment received for invoice #" + invoice.getId(),
                    "CUSTOMER_PAYMENT", saved.getId());
        } else if (payment.getPaymentType() == Payment.PaymentType.VENDOR_PAYMENT) {
            VendorBill bill = vendorBillService.markPaid(payment.getReferenceId());
            journalService.recordDebit("Sealant Supplier Payables", saved.getAmount(),
                    "Vendor payment made for bill #" + bill.getId(),
                    "VENDOR_PAYMENT", saved.getId());
            journalService.recordCredit("Cash/Bank", saved.getAmount(),
                    "Vendor payment made for bill #" + bill.getId(),
                    "VENDOR_PAYMENT", saved.getId());
        }

        return saved;
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}
