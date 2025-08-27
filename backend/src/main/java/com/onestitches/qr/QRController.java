package com.onestitches.qr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.onestitches.order.OrderService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/qr")
public class QRController {

    private final OrderService orderService;

    public QRController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(value = "/generate/{orderId}", produces = MediaType.IMAGE_PNG_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<byte[]> generate(@PathVariable String orderId) throws WriterException, IOException {
        String payload = "order:" + orderId;
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(payload, BarcodeFormat.QR_CODE, 300, 300);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", out);
        return ResponseEntity.ok()
                .header(HttpHeaders.CACHE_CONTROL, "no-cache")
                .contentType(MediaType.IMAGE_PNG)
                .body(out.toByteArray());
    }

    public record ScanRequest(String code, String nextStatus) {}

    @PostMapping("/scan")
    @PreAuthorize("hasAnyRole('FACTORY','ADMIN')")
    public Map<String, String> scan(@RequestBody ScanRequest request) {
        // Very simple code parsing: code format "order:{id}"
        if (request == null || request.code() == null || !request.code().startsWith("order:")) {
            throw new IllegalArgumentException("invalid code");
        }
        String orderId = request.code().substring("order:".length());
        String status = switch (request.nextStatus()) {
            case "IN_PROGRESS", "READY", "SHIPPED", "DELIVERED" -> request.nextStatus();
            default -> throw new IllegalArgumentException("invalid status");
        };
        // For this stub, we publish status updates without enforcing transitions on a real entity lookup
        orderService.markConfirmed(orderId); // reuse to trigger publish; in real code, update by orderId
        return Map.of("orderId", orderId, "status", status);
    }
}


