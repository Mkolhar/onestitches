package com.onestitches.order;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    /**
     * Creates an order after verifying payment and inventory.
     * @return created order
     */
    @PostMapping
    public ResponseEntity<String> create() {
        // TODO: verify Stripe PaymentIntent status
        // TODO: ensure idempotency via Redis key
        // TODO: reserve inventory and persist order
        // TODO: publish order.created event
        return ResponseEntity.status(201).body("stub");
    }
}
