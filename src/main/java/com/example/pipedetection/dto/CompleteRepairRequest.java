package com.example.pipedetection.dto;

/** Request body used when marking a leak repair as completed. */
public class CompleteRepairRequest {

    private String repairNotes;

    public String getRepairNotes() {
        return repairNotes;
    }

    public void setRepairNotes(String repairNotes) {
        this.repairNotes = repairNotes;
    }
}
