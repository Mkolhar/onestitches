package com.onestitches.order;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusPublisher {
    private final SimpMessagingTemplate template;

    public OrderStatusPublisher(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void publishStatus(String orderId, String status) {
        template.convertAndSend("/topic/order/" + orderId, status);
    }
}


