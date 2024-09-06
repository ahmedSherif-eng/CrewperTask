package com.example.crewper.services;

import com.example.crewper.entities.Orders;
import com.example.crewper.model.OrderDTO;
import com.example.crewper.repositories.OrderRepo;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Setter
@Getter
@Named
public class AcceptedOrderServiceImpl implements OrderService, JavaDelegate {
    private final OrderRepo orderRepo;
    private final ModelMapper modelMapper;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        boolean accepted = (boolean) delegateExecution.getVariable("OrderAccepted");
        OrderDTO orderDTO = (OrderDTO) delegateExecution.getVariable("order");
        createOrder(orderDTO);
    }

    @Override
    public void createOrder(OrderDTO orderDTO) {
        Orders order = modelMapper.map(orderDTO, Orders.class);
        order.setDate(java.time.LocalDateTime.now());
        order.setStatus("NEW");
        orderRepo.save(order);
        System.out.println("Order created");
    }

}
