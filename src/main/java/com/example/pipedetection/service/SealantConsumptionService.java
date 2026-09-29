package com.example.pipedetection.service;

import com.example.pipedetection.entity.Product;
import com.example.pipedetection.entity.SealantConsumption;
import com.example.pipedetection.exception.BusinessRuleException;
import com.example.pipedetection.repository.SealantConsumptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SealantConsumptionService {

    @Autowired
    private SealantConsumptionRepository sealantConsumptionRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private JournalService journalService;

    /**
     * Records epoxy resin sealant consumed for a leak repair.
     * Reduces the product's stock quantity and writes a simple expense journal entry.
     */
    public SealantConsumption recordConsumption(Long leakId, Long productId, Integer quantityUsed) {
        Product product = productService.getProductById(productId);

        if (product.getStockQuantity() == null || product.getStockQuantity() < quantityUsed) {
            throw new BusinessRuleException("Insufficient sealant stock for product: " + product.getName()
                    + " (available: " + product.getStockQuantity() + ", requested: " + quantityUsed + ")");
        }

        product.setStockQuantity(product.getStockQuantity() - quantityUsed);
        productService.updateProduct(product.getId(), product);

        SealantConsumption consumption = new SealantConsumption();
        consumption.setLeakId(leakId);
        consumption.setProductId(productId);
        consumption.setQuantityUsed(quantityUsed);
        SealantConsumption saved = sealantConsumptionRepository.save(consumption);

        BigDecimal amount = product.getUnitPrice().multiply(BigDecimal.valueOf(quantityUsed));
        journalService.recordDebit("Sealant Material Costs", amount,
                "Sealant consumed for leak #" + leakId, "SEALANT_CONSUMPTION", saved.getId());

        return saved;
    }

    public List<SealantConsumption> getAllConsumption() {
        return sealantConsumptionRepository.findAll();
    }

    public List<SealantConsumption> getConsumptionForLeak(Long leakId) {
        return sealantConsumptionRepository.findByLeakId(leakId);
    }
}
