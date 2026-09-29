package com.example.pipedetection.controller;

import com.example.pipedetection.entity.Drone;
import com.example.pipedetection.service.DroneService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pipes/drones")
@CrossOrigin(origins = "*")
public class DroneController {

    @Autowired
    private DroneService droneService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Drone createDrone(@Valid @RequestBody Drone drone) {
        return droneService.createDrone(drone);
    }

    @GetMapping
    public List<Drone> getAllDrones() {
        return droneService.getAllDrones();
    }

    @GetMapping("/{id}")
    public Drone getDrone(@PathVariable Long id) {
        return droneService.getDroneById(id);
    }

    @PutMapping("/{id}")
    public Drone updateDrone(@PathVariable Long id, @Valid @RequestBody Drone drone) {
        return droneService.updateDrone(id, drone);
    }

    @DeleteMapping("/{id}")
    public void deleteDrone(@PathVariable Long id) {
        droneService.deleteDrone(id);
    }

    @PostMapping("/{id}/dispatch")
    public Drone dispatchDrone(@PathVariable Long id) {
        return droneService.dispatchDrone(id);
    }

    @PostMapping("/{id}/mark-available")
    public Drone markAvailable(@PathVariable Long id) {
        return droneService.markAvailable(id);
    }

    @PostMapping("/{id}/mark-maintenance")
    public Drone markMaintenance(@PathVariable Long id) {
        return droneService.markMaintenance(id);
    }
}
