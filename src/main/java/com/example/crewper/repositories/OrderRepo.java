package com.example.crewper.repositories;


import com.example.crewper.entities.Orders;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface OrderRepo extends JpaRepository<Orders, Long> , PagingAndSortingRepository<Orders, Long> {

}
