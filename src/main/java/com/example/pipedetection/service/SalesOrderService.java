package com.example.pipedetection.service;

import com.example.pipedetection.dto.OrderItemRequest;
import com.example.pipedetection.dto.SalesOrderDetailResponse;
import com.example.pipedetection.dto.SalesOrderRequest;
import com.example.pipedetection.entity.Product;
import com.example.pipedetection.entity.SalesOrder;
import com.example.pipedetection.entity.SalesOrderItem;
import com.example.pipedetection.exception.ResourceNotFoundException;
import com.example.pipedetection.repository.SalesOrderItemRepository;
import com.example.pipedetection.repository.SalesOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class SalesOrderService {

    @Autowired
    private SalesOrderRepository salesOrderRepository;

    @Autowired
    private SalesOrderItemRepository salesOrderItemRepository;

    @Autowired
    private ProductService productService;

    /** Creates a sales order (e.g. monthly pipeline maintenance) together with its line items. */
    public SalesOrder createSalesOrder(SalesOrderRequest request) {
        SalesOrder order = new SalesOrder();
        order.setCustomerId(request.getCustomerId());
        order.setOrderDate(request.getOrderDate() != null ? request.getOrderDate() : LocalDate.now());
        order.setAnalyticAccountId(request.getAnalyticAccountId());
        order.setStatus(SalesOrder.SOStatus.DRAFT);
        order.setTotalAmount(BigDecimal.ZERO);
        SalesOrder savedOrder = salesOrderRepository.save(order);

        BigDecimal total = BigDecimal.ZERO;
        for (OrderItemRequest itemRequest : request.getItems()) {
            Product product = productService.getProductById(itemRequest.getProductId());
            BigDecimal unitPrice = itemRequest.getUnitPrice() != null ? itemRequest.getUnitPrice() : product.getUnitPrice();
            BigDecimal amount = unitPrice.multiply(BigDecimal.valueOf(itemRequest.getQuantity()));

            SalesOrderItem item = new SalesOrderItem();
            item.setSalesOrderId(savedOrder.getId());
            item.setProductId(product.getId());
            item.setQuantity(itemRequest.getQuantity());
            item.setUnitPrice(unitPrice);
            item.setAmount(amount);
            salesOrderItemRepository.save(item);

            total = total.add(amount);
        }

        savedOrder.setTotalAmount(total);
        return salesOrderRepository.save(savedOrder);
    }

    public List<SalesOrder> getAllSalesOrders() {
        return salesOrderRepository.findAll();
    }

    public SalesOrder getSalesOrderById(Long id) {
        return salesOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sales order not found with id: " + id));
    }

    public SalesOrderDetailResponse getSalesOrderDetail(Long id) {
        SalesOrder order = getSalesOrderById(id);
        List<SalesOrderItem> items = salesOrderItemRepository.findBySalesOrderId(id);
        return new SalesOrderDetailResponse(order, items);
    }

    public SalesOrder markCompleted(Long id) {
        SalesOrder order = getSalesOrderById(id);
        order.setStatus(SalesOrder.SOStatus.COMPLETED);
        return salesOrderRepository.save(order);
    }
}
