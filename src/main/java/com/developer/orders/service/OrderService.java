package com.developer.orders.service;

import com.developer.orders.entity.OrderEntity;
import com.developer.orders.exceptions.OrderNotFoundException; // You might need to create this exception
import com.developer.orders.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<OrderEntity> getAllOrders() {
        return orderRepository.findAll();
    }

    public OrderEntity createOrder(OrderEntity order) {
        return orderRepository.save(order);
    }

    public Optional<OrderEntity> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Transactional
    public OrderEntity updateOrder(Long orderId, OrderEntity updatedOrder) {
        Optional<OrderEntity> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            throw new OrderNotFoundException("Order not found with ID: " + orderId);
        }

        OrderEntity existingOrder = optionalOrder.get();

        // Apply changes from the updatedOrder to the existingOrder
        // Assuming you have setters in OrderEntity or you can directly assign if using @Data from Lombok
        existingOrder.setQuantity(updatedOrder.getQuantity());
        existingOrder.setAddress(updatedOrder.getAddress());
        existingOrder.setDate(updatedOrder.getDate());
        existingOrder.setStatus(updatedOrder.getStatus());

        return orderRepository.save(existingOrder);
    }

    public void deactivateOrder(Long orderId) {
        Optional<OrderEntity> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            OrderEntity order = orderOptional.get();
            order.setStatus("INACTIVE"); // Assuming you manage the status as a String. Adjust according to your design.
            orderRepository.save(order);
        } else {
            throw new OrderNotFoundException("Order not found with ID: " + orderId);
        }
    }
}
