package com.example.pipedetection.service;

import com.example.pipedetection.entity.Drone;
import com.example.pipedetection.exception.BusinessRuleException;
import com.example.pipedetection.exception.ResourceNotFoundException;
import com.example.pipedetection.repository.DroneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DroneService {

    @Autowired
    private DroneRepository droneRepository;

    public Drone createDrone(Drone drone) {
        return droneRepository.save(drone);
    }

    public List<Drone> getAllDrones() {
        return droneRepository.findAll();
    }

    public Drone getDroneById(Long id) {
        return droneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Drone not found with id: " + id));
    }

    public Drone updateDrone(Long id, Drone updated) {
        Drone existing = getDroneById(id);
        existing.setDroneCode(updated.getDroneCode());
        existing.setDroneName(updated.getDroneName());
        existing.setStatus(updated.getStatus());
        existing.setBatteryLevel(updated.getBatteryLevel());
        existing.setCurrentLocation(updated.getCurrentLocation());
        return droneRepository.save(existing);
    }

    public void deleteDrone(Long id) {
        Drone existing = getDroneById(id);
        droneRepository.delete(existing);
    }

    public Drone markAvailable(Long id) {
        Drone drone = getDroneById(id);
        drone.setStatus(Drone.DroneStatus.AVAILABLE);
        return droneRepository.save(drone);
    }

    public Drone markMaintenance(Long id) {
        Drone drone = getDroneById(id);
        drone.setStatus(Drone.DroneStatus.MAINTENANCE);
        return droneRepository.save(drone);
    }

    public Drone dispatchDrone(Long id) {
        Drone drone = getDroneById(id);
        if (drone.getStatus() != Drone.DroneStatus.AVAILABLE) {
            throw new BusinessRuleException("Drone " + drone.getDroneCode() + " is not available for dispatch (current status: " + drone.getStatus() + ")");
        }
        drone.setStatus(Drone.DroneStatus.DISPATCHED);
        return droneRepository.save(drone);
    }

    /** Finds the first available drone, used when the caller does not specify which drone to dispatch. */
    public Drone findAnyAvailableDrone() {
        Optional<Drone> drone = droneRepository.findFirstByStatus(Drone.DroneStatus.AVAILABLE);
        return drone.orElseThrow(() -> new BusinessRuleException("No drone is currently available for dispatch"));
    }
}
