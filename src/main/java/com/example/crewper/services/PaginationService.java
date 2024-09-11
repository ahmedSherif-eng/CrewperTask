package com.example.crewper.services;

import com.example.crewper.entities.Orders;
import com.example.crewper.repositories.OrderRepo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PaginationService {
    private final OrderRepo orderRepo;
    public Page<Orders> getOrders(int page, int size, String sortBy) {
        return orderRepo.findAll(PageRequest.of(page, size, Sort.by(sortBy)));
    }
}
