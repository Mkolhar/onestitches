package com.onestitches.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Request payload for creating an order.
 */
public record CreateOrderRequest(
        @NotBlank String paymentIntentId,
        @NotEmpty List<@Valid OrderItem> items
) {}
