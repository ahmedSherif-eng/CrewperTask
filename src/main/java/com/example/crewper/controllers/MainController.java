package com.example.crewper.controllers;

import com.example.crewper.entities.Orders;
import com.example.crewper.model.OrderDTO;

import com.example.crewper.services.PaginationService;
import com.example.crewper.services.UpdateOrderService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
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
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<?> getOrder(@PathVariable("orderId") Long orderId) {
        Orders order = orderService.getOrder(orderId);
        if (order != null) {
            return ResponseEntity.ok(order);
        } else {
            return ResponseEntity.status(404).body("Order not found.");
        }
    }

    @PutMapping("/orders/{orderId}/status")
    public ResponseEntity<String> updateOrderStatus(@PathVariable("orderId") Long orderId,
                                                    @RequestParam(name = "orderStatus") String orderStatus) {
        if (!orderStatus.equals("ACCEPTED") && !orderStatus.equals("REJECTED")  && !orderStatus.equals("PENDING") && !orderStatus.equals("COMPLETED") ) {
            return ResponseEntity.badRequest().body("Invalid status: Accepted, Rejected, Pending, Completed");
        }
        boolean updated = orderService.updateOrderStatus(orderId, orderStatus);
        if (updated) {
            return ResponseEntity.ok("Order status updated successfully.");
        } else {
            return ResponseEntity.status(404).body("Order not found.");
        }
    }
    @GetMapping("/orders")
    public ResponseEntity<?> getOrders(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy) {
            if (page < 0 || size < 1) {
                return ResponseEntity.badRequest().body("Invalid page or size parameter.");
            }
            if (!sortBy.equals("id") && !sortBy.equals("name") && !sortBy.equals("date") && !sortBy.equals("status")) {
                return ResponseEntity.badRequest().body("Invalid sortBy parameter. Use id, name, date, or status.");
            }
        return ResponseEntity.status(200).body(paginationService.getOrders(page, size, sortBy));
    }
    @GetMapping("/deploy")
    public void deployProcess() {
        repositoryService.createDeployment().addClasspathResource("CreateOrder.bpmn").deploy();
    }

    @Deprecated //do not use this endpoint in production
    //@GetMapping("/orders/{orderId}")
    public Orders DeprecatedgetOrder(@PathVariable("orderId") Long orderId) throws InterruptedException {
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
