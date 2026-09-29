package com.example.pipedetection.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "journal_entries")
public class JournalEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Transaction date is required")
    @Column(nullable = false)
    private LocalDate transactionDate = LocalDate.now();

    @NotNull(message = "Account id is required")
    @Column(nullable = false)
    private Long accountId;

    private String description;

    @Column(nullable = false)
    private BigDecimal debit = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal credit = BigDecimal.ZERO;

    /** e.g. CUSTOMER_INVOICE, CUSTOMER_PAYMENT, VENDOR_BILL, VENDOR_PAYMENT, SEALANT_CONSUMPTION, REPAIR_REVENUE */
    private String referenceType;

    private Long referenceId;

    public JournalEntry() {
    }

    public JournalEntry(LocalDate transactionDate, Long accountId, String description,
                         BigDecimal debit, BigDecimal credit, String referenceType, Long referenceId) {
        this.transactionDate = transactionDate;
        this.accountId = accountId;
        this.description = description;
        this.debit = debit;
        this.credit = credit;
        this.referenceType = referenceType;
        this.referenceId = referenceId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getDebit() {
        return debit;
    }

    public void setDebit(BigDecimal debit) {
        this.debit = debit;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    public Long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }
}
