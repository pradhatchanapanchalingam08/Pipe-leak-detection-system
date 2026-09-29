package com.example.pipedetection.entity;

import jakarta.persistence.*;

/**
 * Holds simple system-wide settings for the demo, such as the
 * acoustic leak tolerance limit configured by the Admin.
 * A single row (id = 1) is used and updated in place.
 */
@Entity
@Table(name = "system_settings")
public class SystemSetting {

    @Id
    private Long id = 1L;

    /** Acoustic level above which a leak is considered out of tolerance. */
    @Column(nullable = false)
    private Double leakToleranceLimit = 70.0;

    public SystemSetting() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLeakToleranceLimit() {
        return leakToleranceLimit;
    }

    public void setLeakToleranceLimit(Double leakToleranceLimit) {
        this.leakToleranceLimit = leakToleranceLimit;
    }
}
