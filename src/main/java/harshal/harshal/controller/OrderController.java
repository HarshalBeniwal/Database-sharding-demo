package harshal.harshal.controller;

import harshal.harshal.dto.OrderRequestDTO;
import harshal.harshal.entity.Order;
import harshal.harshal.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Log4j2
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<String> saveOrder(@RequestBody @Valid OrderRequestDTO orderRequestDTO){
        orderService.saveOrder(orderRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Order processed successfully");
    }

    @GetMapping
    public ResponseEntity<List<Order>> getOrdersGreaterThanAmount(@RequestParam BigDecimal amount){
        return  ResponseEntity
                .status(HttpStatus.OK)
                .body(orderService.getOrdersGreaterThanAmount(amount));
    }
}
