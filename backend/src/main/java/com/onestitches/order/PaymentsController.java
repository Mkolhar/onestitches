package com.onestitches.order;

import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentsController {

    private final OrderService orderService;

    public PaymentsController(OrderService orderService) {
        this.orderService = orderService;
        Stripe.apiKey = System.getenv().getOrDefault("STRIPE_SECRET_KEY", "");
    }

    public record CreateIntentItem(String sku, int qty) {}
    public record CreateIntentRequest(List<CreateIntentItem> items) {}

    @PostMapping("/intent")
    @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
    public Map<String, String> createIntent(@RequestBody CreateIntentRequest request) throws StripeException {
        if (request == null || request.items() == null || request.items().isEmpty()) {
            throw new IllegalArgumentException("items required");
        }
        // compute amount from known catalog stub
        long amount = 0L;
        List<Order.Item> orderItems = new ArrayList<>();
        for (CreateIntentItem it : request.items()) {
            double price;
            switch (it.sku()) {
                case "TSHIRT-001" -> price = 499.0;
                case "HOODIE-001" -> price = 999.0;
                case "MUG-001" -> price = 299.0;
                default -> throw new IllegalArgumentException("unknown sku: " + it.sku());
            }
            amount += Math.round(price * 100) * (long) it.qty();
            orderItems.add(new Order.Item(it.sku(), it.qty()));
        }

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setCurrency("inr")
                .setAmount(amount)
                .addPaymentMethodType("upi")
                .build();
        PaymentIntent pi = PaymentIntent.create(params);

        // persist a pending order linked to payment intent
        orderService.createOrder("anon", orderItems, pi.getId(), amount);

        return Map.of("clientSecret", pi.getClientSecret());
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> webhook(@RequestBody String payload, @RequestHeader(value = "Stripe-Signature", required = false) String sigHeader) {
        String endpointSecret = System.getenv().getOrDefault("STRIPE_WEBHOOK_SECRET", "");
        Event event;
        try {
            if (endpointSecret != null && !endpointSecret.isBlank() && sigHeader != null) {
                event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
            } else {
                event = Event.GSON.fromJson(payload, Event.class); // dev fallback without verification
            }
        } catch (SignatureVerificationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if ("payment_intent.succeeded".equals(event.getType())) {
            PaymentIntent pi = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);
            if (pi != null) {
                orderService.markConfirmed(pi.getId());
            }
        } else if ("payment_intent.payment_failed".equals(event.getType())) {
            PaymentIntent pi = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);
            if (pi != null) {
                orderService.markFailed(pi.getId());
            }
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}


