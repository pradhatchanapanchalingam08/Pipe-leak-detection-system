package com.example.pipedetection.service;

import com.example.pipedetection.dto.DashboardSummaryResponse;
import com.example.pipedetection.entity.CustomerInvoice;
import com.example.pipedetection.entity.Drone;
import com.example.pipedetection.entity.Leak;
import com.example.pipedetection.entity.VendorBill;
import com.example.pipedetection.repository.CustomerInvoiceRepository;
import com.example.pipedetection.repository.DroneRepository;
import com.example.pipedetection.repository.LeakRepository;
import com.example.pipedetection.repository.VendorBillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    @Autowired
    private DroneRepository droneRepository;

    @Autowired
    private LeakRepository leakRepository;

    @Autowired
    private CustomerInvoiceRepository customerInvoiceRepository;

    @Autowired
    private VendorBillRepository vendorBillRepository;

    public DashboardSummaryResponse getSummary() {
        long totalDrones = droneRepository.count();
        long availableDrones = droneRepository.findByStatus(Drone.DroneStatus.AVAILABLE).size();

        long activeLeaks = leakRepository.findAll().stream()
                .filter(l -> l.getStatus() != Leak.LeakStatus.REPAIRED)
                .count();
        long repairedLeaks = leakRepository.findAll().stream()
                .filter(l -> l.getStatus() == Leak.LeakStatus.REPAIRED)
                .count();

        long unpaidInvoices = customerInvoiceRepository.findAll().stream()
                .filter(i -> i.getStatus() == CustomerInvoice.InvoiceStatus.UNPAID)
                .count();
        long unpaidBills = vendorBillRepository.findAll().stream()
                .filter(b -> b.getStatus() == VendorBill.VendorBillStatus.UNPAID)
                .count();

        return new DashboardSummaryResponse(totalDrones, availableDrones, activeLeaks,
                repairedLeaks, unpaidInvoices, unpaidBills);
    }
}
