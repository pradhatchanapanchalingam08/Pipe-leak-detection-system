package com.example.pipedetection.service;

import com.example.pipedetection.dto.CreateVendorBillRequest;
import com.example.pipedetection.entity.VendorBill;
import com.example.pipedetection.exception.BusinessRuleException;
import com.example.pipedetection.exception.ResourceNotFoundException;
import com.example.pipedetection.repository.VendorBillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class VendorBillService {

    @Autowired
    private VendorBillRepository vendorBillRepository;

    @Autowired
    private JournalService journalService;

    /** Creates a vendor bill for epoxy resin cartridges (or any other supplier purchase). */
    public VendorBill createVendorBill(CreateVendorBillRequest request) {
        VendorBill bill = new VendorBill();
        bill.setSupplierId(request.getSupplierId());
        bill.setPurchaseOrderId(request.getPurchaseOrderId());
        bill.setBillDate(request.getBillDate() != null ? request.getBillDate() : LocalDate.now());
        bill.setAmount(request.getAmount());
        bill.setStatus(VendorBill.VendorBillStatus.UNPAID);
        VendorBill saved = vendorBillRepository.save(bill);

        journalService.recordDebit("Sealant Material Costs", saved.getAmount(),
                "Vendor bill #" + saved.getId() + " received", "VENDOR_BILL", saved.getId());
        journalService.recordCredit("Sealant Supplier Payables", saved.getAmount(),
                "Vendor bill #" + saved.getId() + " received", "VENDOR_BILL", saved.getId());

        return saved;
    }

    public List<VendorBill> getAllVendorBills() {
        return vendorBillRepository.findAll();
    }

    public VendorBill getVendorBillById(Long id) {
        return vendorBillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor bill not found with id: " + id));
    }

    public VendorBill markPaid(Long id) {
        VendorBill bill = getVendorBillById(id);
        if (bill.getStatus() == VendorBill.VendorBillStatus.PAID) {
            throw new BusinessRuleException("Vendor bill #" + id + " is already marked as PAID");
        }
        bill.setStatus(VendorBill.VendorBillStatus.PAID);
        return vendorBillRepository.save(bill);
    }
}
