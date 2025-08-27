package com.onestitches.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * Line item for an order.
 */
public record OrderItem(
        @NotBlank String sku,
        @Min(1) int qty
) {}
