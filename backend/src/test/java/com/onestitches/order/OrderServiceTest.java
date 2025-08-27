package com.onestitches.order;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

class OrderServiceTest {
    OrderService service = new OrderService();

    @Test
    void idempotentOnPaymentIntent() {
        CreateOrderRequest req = new CreateOrderRequest("pi_1", List.of(new OrderItem("SKU",1)));
        Order first = service.createOrder(req);
        Order second = service.createOrder(req);
        assertThat(second.id()).isEqualTo(first.id());
    }
}
