package com.example.pipedetection.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "leaks")
public class Leak {

    public enum LeakSeverity {
        LOW,
        MEDIUM,
        HIGH
    }

    public enum LeakStatus {
        DETECTED,
        DRONE_DISPATCHED,
        REPAIR_IN_PROGRESS,
        REPAIRED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Pipe location is required")
    @Column(nullable = false)
    private String pipeLocation;

    @NotNull(message = "Acoustic level is required")
    @Column(nullable = false)
    private Double acousticLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeakSeverity leakSeverity;

    @Column(nullable = false)
    private LocalDateTime detectedAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeakStatus status = LeakStatus.DETECTED;

    /** Foreign key reference to Drone.id -- kept as a plain Long for simplicity. */
    private Long assignedDroneId;

    @Column(length = 1000)
    private String repairNotes;

    /** Optional foreign key reference to AnalyticAccount.id (pipeline sector). */
    private Long analyticAccountId;

    public Leak() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPipeLocation() {
        return pipeLocation;
    }

    public void setPipeLocation(String pipeLocation) {
        this.pipeLocation = pipeLocation;
    }

    public Double getAcousticLevel() {
        return acousticLevel;
    }

    public void setAcousticLevel(Double acousticLevel) {
        this.acousticLevel = acousticLevel;
    }

    public LeakSeverity getLeakSeverity() {
        return leakSeverity;
    }

    public void setLeakSeverity(LeakSeverity leakSeverity) {
        this.leakSeverity = leakSeverity;
    }

    public LocalDateTime getDetectedAt() {
        return detectedAt;
    }

    public void setDetectedAt(LocalDateTime detectedAt) {
        this.detectedAt = detectedAt;
    }

    public LeakStatus getStatus() {
        return status;
    }

    public void setStatus(LeakStatus status) {
        this.status = status;
    }

    public Long getAssignedDroneId() {
        return assignedDroneId;
    }

    public void setAssignedDroneId(Long assignedDroneId) {
        this.assignedDroneId = assignedDroneId;
    }

    public String getRepairNotes() {
        return repairNotes;
    }

    public void setRepairNotes(String repairNotes) {
        this.repairNotes = repairNotes;
    }

    public Long getAnalyticAccountId() {
        return analyticAccountId;
    }

    public void setAnalyticAccountId(Long analyticAccountId) {
        this.analyticAccountId = analyticAccountId;
    }
}
