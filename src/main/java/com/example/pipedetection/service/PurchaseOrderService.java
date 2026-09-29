package com.example.pipedetection.service;

import com.example.pipedetection.dto.OrderItemRequest;
import com.example.pipedetection.dto.PurchaseOrderDetailResponse;
import com.example.pipedetection.dto.PurchaseOrderRequest;
import com.example.pipedetection.entity.Product;
import com.example.pipedetection.entity.PurchaseOrder;
import com.example.pipedetection.entity.PurchaseOrderItem;
import com.example.pipedetection.exception.ResourceNotFoundException;
import com.example.pipedetection.repository.PurchaseOrderItemRepository;
import com.example.pipedetection.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class PurchaseOrderService {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private PurchaseOrderItemRepository purchaseOrderItemRepository;

    @Autowired
    private ProductService productService;

    /** Creates a purchase order together with its line items. Total amount is computed from the items. */
    public PurchaseOrder createPurchaseOrder(PurchaseOrderRequest request) {
        PurchaseOrder order = new PurchaseOrder();
        order.setSupplierId(request.getSupplierId());
        order.setOrderDate(request.getOrderDate() != null ? request.getOrderDate() : LocalDate.now());
        order.setStatus(PurchaseOrder.POStatus.DRAFT);
        order.setTotalAmount(BigDecimal.ZERO);
        PurchaseOrder savedOrder = purchaseOrderRepository.save(order);

        BigDecimal total = BigDecimal.ZERO;
        for (OrderItemRequest itemRequest : request.getItems()) {
            Product product = productService.getProductById(itemRequest.getProductId());
            BigDecimal unitPrice = itemRequest.getUnitPrice() != null ? itemRequest.getUnitPrice() : product.getUnitPrice();
            BigDecimal amount = unitPrice.multiply(BigDecimal.valueOf(itemRequest.getQuantity()));

            PurchaseOrderItem item = new PurchaseOrderItem();
            item.setPurchaseOrderId(savedOrder.getId());
            item.setProductId(product.getId());
            item.setQuantity(itemRequest.getQuantity());
            item.setUnitPrice(unitPrice);
            item.setAmount(amount);
            purchaseOrderItemRepository.save(item);

            total = total.add(amount);
        }

        savedOrder.setTotalAmount(total);
        return purchaseOrderRepository.save(savedOrder);
    }

    public List<PurchaseOrder> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll();
    }

    public PurchaseOrder getPurchaseOrderById(Long id) {
        return purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase order not found with id: " + id));
    }

    public PurchaseOrderDetailResponse getPurchaseOrderDetail(Long id) {
        PurchaseOrder order = getPurchaseOrderById(id);
        List<PurchaseOrderItem> items = purchaseOrderItemRepository.findByPurchaseOrderId(id);
        return new PurchaseOrderDetailResponse(order, items);
    }

    public PurchaseOrder updateStatus(Long id, PurchaseOrder.POStatus status) {
        PurchaseOrder order = getPurchaseOrderById(id);
        order.setStatus(status);
        return purchaseOrderRepository.save(order);
    }
}
