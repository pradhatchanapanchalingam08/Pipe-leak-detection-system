package com.example.pipedetection.repository;

import com.example.pipedetection.entity.SealantConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SealantConsumptionRepository extends JpaRepository<SealantConsumption, Long> {
    List<SealantConsumption> findByLeakId(Long leakId);
}
