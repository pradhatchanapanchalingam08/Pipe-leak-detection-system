package com.example.pipedetection.dto;

/** Simple counts shown as cards on the main dashboard page. */
public class DashboardSummaryResponse {

    private long totalDrones;
    private long availableDrones;
    private long activeLeaks;
    private long repairedLeaks;
    private long unpaidCustomerInvoices;
    private long unpaidVendorBills;

    public DashboardSummaryResponse(long totalDrones, long availableDrones, long activeLeaks,
                                     long repairedLeaks, long unpaidCustomerInvoices, long unpaidVendorBills) {
        this.totalDrones = totalDrones;
        this.availableDrones = availableDrones;
        this.activeLeaks = activeLeaks;
        this.repairedLeaks = repairedLeaks;
        this.unpaidCustomerInvoices = unpaidCustomerInvoices;
        this.unpaidVendorBills = unpaidVendorBills;
    }

    public long getTotalDrones() {
        return totalDrones;
    }

    public long getAvailableDrones() {
        return availableDrones;
    }

    public long getActiveLeaks() {
        return activeLeaks;
    }

    public long getRepairedLeaks() {
        return repairedLeaks;
    }

    public long getUnpaidCustomerInvoices() {
        return unpaidCustomerInvoices;
    }

    public long getUnpaidVendorBills() {
        return unpaidVendorBills;
    }
}
