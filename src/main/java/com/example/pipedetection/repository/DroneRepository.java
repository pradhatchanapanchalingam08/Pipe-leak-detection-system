package com.example.pipedetection.repository;

import com.example.pipedetection.entity.Drone;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DroneRepository extends JpaRepository<Drone, Long> {
    List<Drone> findByStatus(Drone.DroneStatus status);
    Optional<Drone> findFirstByStatus(Drone.DroneStatus status);
}
