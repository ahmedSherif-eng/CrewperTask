package com.example.crewper.services;

import com.example.crewper.entities.Orders;
import com.example.crewper.repositories.OrderRepo;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.util.Map;
@Named
@RequiredArgsConstructor
@Service
public class OrderDetails implements JavaDelegate {
    private final OrderRepo orderRepo;
    private final RuntimeService runtimeService;
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Map<String, Object> variables = delegateExecution.getVariables();
        Orders order = (Orders) getOrder((long) delegateExecution.getVariable("orderId"));
        variables.put("order",order);
        runtimeService.setVariable(delegateExecution.getProcessInstanceId(), "order", order);
    }
    private Orders getOrder(long orderId) {
        Orders order = orderRepo.findById(orderId).orElse(null);
        System.out.println(order.getName());
        return order;
    }
}
