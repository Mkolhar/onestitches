package com.onestitches.order;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory order service that ensures idempotent creation based on payment intent ID.
 */
@Service
public class OrderService {
    private final Map<String, Order> ordersByPaymentIntent = new ConcurrentHashMap<>();

    public Order createOrder(CreateOrderRequest req) {
        if (!req.paymentIntentId().startsWith("pi_")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid payment intent");
        }
        return ordersByPaymentIntent.computeIfAbsent(req.paymentIntentId(), id ->
                new Order(UUID.randomUUID().toString(), req.items(), id, "created"));
    }
}
