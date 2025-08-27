package com.onestitches.order;

import java.util.List;

/**
 * Simple order representation for the MVP.
 */
public record Order(
        String id,
        List<OrderItem> items,
        String paymentIntentId,
        String status
) {}
