package com.onestitches.order;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/uploads")
public class UploadController {
    private static final long MAX_FILE_SIZE = 25 * 1024 * 1024; // 25MB
    private static final List<String> ALLOWED_TYPES = List.of("image/png", "image/jpeg", "image/svg+xml");

    /**
     * Simple in-memory token store. In production this would be Redis or database.
     */
    private final Map<String, Boolean> tokens = new ConcurrentHashMap<>();

    /**
     * Issues a one-time upload URL.
     * @param fileName original file name (unused but kept for parity with real presign)
     * @return map containing upload URL
     */
    @PostMapping("/presign")
    public Map<String, String> presign(@RequestParam("fileName") String fileName) {
        String token = UUID.randomUUID().toString();
        tokens.put(token, Boolean.TRUE);
        return Map.of("url", "/api/uploads/" + token);
    }

    /**
     * Accepts the file upload using a previously issued token.
     */
    @PutMapping(value = "/{token}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> upload(@PathVariable String token, @RequestParam("file") MultipartFile file) {
        if (!tokens.containsKey(token)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("invalid token");
        }
        tokens.remove(token); // single use
        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            return ResponseEntity.badRequest().body("unsupported type");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("file too large");
        }
        // In a real implementation the file would be streamed to object storage and a URL returned.
        return ResponseEntity.status(HttpStatus.CREATED).body("ok");
    }
}
