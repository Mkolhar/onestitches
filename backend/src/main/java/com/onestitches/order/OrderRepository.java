package com.onestitches.order;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {
    private final Map<String, Order> byId = new ConcurrentHashMap<>();
    private final Map<String, String> byPaymentIntent = new ConcurrentHashMap<>();

    public Order save(Order order) {

        byId.put(order.id(), order);
        if (order.paymentIntentId() != null) {
            byPaymentIntent.put(order.paymentIntentId(), order.id());
        }
        return order;
    }

    public Optional<Order> findByPaymentIntentId(String paymentIntentId) {
        String id = byPaymentIntent.get(paymentIntentId);
        return id == null ? Optional.empty() : Optional.ofNullable(byId.get(id));
    }
}


