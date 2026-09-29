package com.example.pipedetection.repository;

import com.example.pipedetection.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
