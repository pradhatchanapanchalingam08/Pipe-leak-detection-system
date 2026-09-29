package com.example.pipedetection.service;

import com.example.pipedetection.entity.Drone;
import com.example.pipedetection.entity.Leak;
import com.example.pipedetection.entity.Product;
import com.example.pipedetection.exception.BusinessRuleException;
import com.example.pipedetection.exception.ResourceNotFoundException;
import com.example.pipedetection.repository.LeakRepository;
import com.example.pipedetection.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class LeakService {

    @Autowired
    private LeakRepository leakRepository;

    @Autowired
    private DroneService droneService;

    @Autowired
    private JournalService journalService;

    @Autowired
    private ProductRepository productRepository;

    private static final String REPAIR_SERVICE_PRODUCT_NAME = "Ultrasonic Pipe Leak Repair";

    /** Simulated acoustic leak alert being received by the system. */
    public Leak createLeak(Leak leak) {
        leak.setStatus(Leak.LeakStatus.DETECTED);
        return leakRepository.save(leak);
    }

    public List<Leak> getAllLeaks() {
        return leakRepository.findAll();
    }

    public Leak getLeakById(Long id) {
        return leakRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leak not found with id: " + id));
    }

    /**
     * Dispatches a repair drone to the leak. If droneId is supplied that specific drone is
     * used (must be AVAILABLE); otherwise the first AVAILABLE drone in the system is picked.
     */
    public Leak dispatchDrone(Long leakId, Long droneId) {
        Leak leak = getLeakById(leakId);

        if (leak.getStatus() != Leak.LeakStatus.DETECTED) {
            throw new BusinessRuleException("A drone has already been dispatched for this leak (status: " + leak.getStatus() + ")");
        }

        Drone drone = (droneId != null) ? droneService.dispatchDrone(droneId)
                                         : droneService.dispatchDrone(droneService.findAnyAvailableDrone().getId());

        leak.setAssignedDroneId(drone.getId());
        leak.setStatus(Leak.LeakStatus.DRONE_DISPATCHED);
        return leakRepository.save(leak);
    }

    /**
     * Marks the repair as completed for a leak that already has a drone assigned.
     * Frees up the drone and records the repair revenue in the journal.
     */
    public Leak completeRepair(Long leakId, String repairNotes) {
        Leak leak = getLeakById(leakId);

        if (leak.getAssignedDroneId() == null) {
            throw new BusinessRuleException("Cannot complete repair: no drone has been dispatched to this leak yet");
        }

        leak.setStatus(Leak.LeakStatus.REPAIRED);
        leak.setRepairNotes(repairNotes);
        Leak saved = leakRepository.save(leak);

        droneService.markAvailable(leak.getAssignedDroneId());

        // Record repair revenue using the "Ultrasonic Pipe Leak Repair" service price, if configured.
        Optional<Product> repairService = productRepository.findAll().stream()
                .filter(p -> REPAIR_SERVICE_PRODUCT_NAME.equalsIgnoreCase(p.getName()))
                .findFirst();
        BigDecimal revenue = repairService.map(Product::getUnitPrice).orElse(BigDecimal.ZERO);
        if (revenue.compareTo(BigDecimal.ZERO) > 0) {
            journalService.recordCredit("Pipe Repair Revenues", revenue,
                    "Repair completed for leak #" + leakId, "REPAIR_REVENUE", leakId);
        }

        return saved;
    }

    /** Marks the leak's status as REPAIR_IN_PROGRESS, e.g. once the drone reaches the site. */
    public Leak markRepairInProgress(Long leakId) {
        Leak leak = getLeakById(leakId);
        if (leak.getStatus() != Leak.LeakStatus.DRONE_DISPATCHED) {
            throw new BusinessRuleException("Leak must have a drone dispatched before repair can start");
        }
        leak.setStatus(Leak.LeakStatus.REPAIR_IN_PROGRESS);
        return leakRepository.save(leak);
    }
}
