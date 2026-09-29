package com.example.pipedetection.repository;

import com.example.pipedetection.entity.CustomerInvoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerInvoiceRepository extends JpaRepository<CustomerInvoice, Long> {
    List<CustomerInvoice> findByCustomerId(Long customerId);
}
