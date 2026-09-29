package com.example.pipedetection.controller;

import com.example.pipedetection.dto.CompleteRepairRequest;
import com.example.pipedetection.dto.DispatchDroneRequest;
import com.example.pipedetection.entity.Leak;
import com.example.pipedetection.service.LeakService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pipes/leaks")
@CrossOrigin(origins = "*")
public class LeakController {

    @Autowired
    private LeakService leakService;

    /** Simulated acoustic leak alert received by the system. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Leak createLeak(@Valid @RequestBody Leak leak) {
        return leakService.createLeak(leak);
    }

    @GetMapping
    public List<Leak> getAllLeaks() {
        return leakService.getAllLeaks();
    }

    @GetMapping("/{id}")
    public Leak getLeak(@PathVariable Long id) {
        return leakService.getLeakById(id);
    }

    @PostMapping("/{id}/dispatch")
    public Leak dispatchDrone(@PathVariable Long id, @RequestBody(required = false) DispatchDroneRequest request) {
        Long droneId = (request != null) ? request.getDroneId() : null;
        return leakService.dispatchDrone(id, droneId);
    }

    @PostMapping("/{id}/start-repair")
    public Leak startRepair(@PathVariable Long id) {
        return leakService.markRepairInProgress(id);
    }

    @PostMapping("/{id}/complete-repair")
    public Leak completeRepair(@PathVariable Long id, @RequestBody(required = false) CompleteRepairRequest request) {
        String notes = (request != null) ? request.getRepairNotes() : null;
        return leakService.completeRepair(id, notes);
    }
}
