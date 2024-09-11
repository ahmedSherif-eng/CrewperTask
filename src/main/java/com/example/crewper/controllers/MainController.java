package com.example.crewper.controllers;

import com.example.crewper.entities.Orders;
import com.example.crewper.model.OrderDTO;
import com.example.crewper.model.OrderStatusDTO;
import com.example.crewper.repositories.OrderRepo;
import com.example.crewper.services.OrderService;
import com.example.crewper.services.PaginationService;
import com.example.crewper.services.UpdateOrderService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.hibernate.query.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/api")
public class MainController {
    private final RuntimeService runtimeService;
    private final RepositoryService repositoryService;
    private final HistoryService historyService;
    private final UpdateOrderService orderService ;
    private final PaginationService paginationService;

    @PostMapping("/orders")
    public void createOrder(@RequestBody OrderDTO orderDTO) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("order", orderDTO);
        runtimeService.startProcessInstanceByKey("Activity_Create_Order", orderDTO.getName());
    }


    @PutMapping("/orders/{orderId}/status")
    public ResponseEntity<String> updateOrderStatus(@PathVariable("orderId") Long orderId, @RequestBody OrderStatusDTO orderStatus) {
        boolean updated = orderService.updateOrderStatus(orderId, orderStatus.getStatus());
        System.out.println(orderStatus.getStatus());
        if (updated) {
            return ResponseEntity.ok("Order status updated successfully.");
        } else {
            return ResponseEntity.status(404).body("Order not found.");
        }
    }
    @GetMapping("/orders")
    public Page<Orders> getOrders(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy) {

        return paginationService.getOrders(page, size, sortBy);
    }
    @GetMapping("/deploy")
    public void deployProcess() {
        repositoryService.createDeployment().addClasspathResource("CreateOrder.bpmn").deploy();
    }

    @Deprecated //do not use this endpoint in production
    //@GetMapping("/orders/{orderId}")
    public Orders getOrders(@PathVariable("orderId") Long orderId) throws InterruptedException {
        Map<String, Object> variables = new HashMap<>();
        variables.put("orderId", orderId);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Activity_Get_Order", variables);
        while (runtimeService.createProcessInstanceQuery().processInstanceId(processInstance.getId()).singleResult() != null) {
            TimeUnit.SECONDS.sleep(2);
        }
        HistoricVariableInstance historicVariable = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(processInstance.getId())
                .variableName("order")
                .singleResult();

        return (Orders) historicVariable.getValue();
    }
}
