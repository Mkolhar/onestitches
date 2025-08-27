package com.onestitches.order;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@Validated
public class OrderController {
    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    /**
     * Creates an order after verifying payment and inventory.
     * @return created order
     */
    @PostMapping
    public ResponseEntity<Order> create(@RequestBody @Validated CreateOrderRequest req) {
        Order order = service.createOrder(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
}
