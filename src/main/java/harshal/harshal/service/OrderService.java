package harshal.harshal.service;

import harshal.harshal.dto.OrderRequestDTO;
import harshal.harshal.entity.Order;
import harshal.harshal.repository.ShardedOrderRepository;
import harshal.harshal.utility.OrderIdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class OrderService {
    private final ShardedOrderRepository shardedOrderRepository;
    private final OrderIdGenerator orderIdGenerator;

    public void saveOrder(OrderRequestDTO requestDTO){
        Order order  =new Order();
        order.setOrderId(UUID.randomUUID());
        order.setAmount(requestDTO.getAmount());
        order.setCreatedAt(Instant.now());
        order.setUserId(requestDTO.getUserId());
        shardedOrderRepository.save(order);
        log.info("Saved order with orderId: {}" , order.getOrderId());

    }

    public List<Order> getOrdersGreaterThanAmount(BigDecimal amount){
        return shardedOrderRepository.getHighValueOrders(amount);
    }
}
