package com.example.crewper.repositories;


import com.example.crewper.entities.Orders;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<Orders, Long> {
}
