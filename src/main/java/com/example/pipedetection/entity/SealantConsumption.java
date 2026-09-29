package com.example.pipedetection.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "sealant_consumption")
public class SealantConsumption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Leak id is required")
    @Column(nullable = false)
    private Long leakId;

    @NotNull(message = "Product id is required")
    @Column(nullable = false)
    private Long productId;

    @NotNull(message = "Quantity used is required")
    @Column(nullable = false)
    private Integer quantityUsed;

    @Column(nullable = false)
    private LocalDateTime consumedAt = LocalDateTime.now();

    public SealantConsumption() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLeakId() {
        return leakId;
    }

    public void setLeakId(Long leakId) {
        this.leakId = leakId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantityUsed() {
        return quantityUsed;
    }

    public void setQuantityUsed(Integer quantityUsed) {
        this.quantityUsed = quantityUsed;
    }

    public LocalDateTime getConsumedAt() {
        return consumedAt;
    }

    public void setConsumedAt(LocalDateTime consumedAt) {
        this.consumedAt = consumedAt;
    }
}
