package com.example.pipedetection.repository;

import com.example.pipedetection.entity.Leak;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeakRepository extends JpaRepository<Leak, Long> {
}
