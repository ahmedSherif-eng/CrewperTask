package com.example.crewper.controllers;

import com.example.crewper.model.OrderDTO;
import com.example.crewper.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.hibernate.query.Order;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/api")
public class MainController {
    private final RuntimeService runtimeService;
    private final RepositoryService repositoryService;
    @PostMapping("/orders")
    public void createOrder(@RequestBody OrderDTO orderDTO) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("order", orderDTO);
        runtimeService.startProcessInstanceByKey("Create_Order_Process", variables);
    }
    @GetMapping("/deploy")
    public void deployProcess() {
        repositoryService.createDeployment().addClasspathResource("CreateOrder.bpmn").deploy();
    }
}
