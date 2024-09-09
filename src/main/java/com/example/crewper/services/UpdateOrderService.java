package com.example.crewper.services;

import com.example.crewper.entities.Orders;
import com.example.crewper.repositories.OrderRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UpdateOrderService {
    private final OrderRepo orderRepo;

    public boolean updateOrderStatus(Long orderId, String status) {
        Orders order = orderRepo.findById(orderId).orElse(null);
        if (order != null) {
            order.setStatus(status);
            orderRepo.save(order);
            return true;
        }
        return false;
    }
}

