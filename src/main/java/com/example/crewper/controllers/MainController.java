package com.example.crewper.controllers;

import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class MainController {
    @PostMapping("/api/orders")
    public String createOrder() {
        return "Order created";
    }
}
