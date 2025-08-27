package com.onestitches.common;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Writes a minimal Postman collection derived from the OpenAPI paths at startup.
 * The file is placed at project root under Docs/postman_collection.json.
 */
@Component
public class PostmanExporter {
    private final OpenAPI openAPI;

    public PostmanExporter(OpenAPI openAPI) {
        this.openAPI = openAPI;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void exportCollection() throws IOException {
        Paths paths = openAPI.getPaths();
        if (paths == null || paths.isEmpty()) return;

        Map<String, Object> collection = new HashMap<>();
        Map<String, Object> info = new HashMap<>();
        info.put("name", "Onestitches API");
        info.put("schema", "https://schema.getpostman.com/json/collection/v2.1.0/collection.json");
        info.put("_exportedAt", Instant.now().toString());
        collection.put("info", info);

        // Build items from OpenAPI paths (only method + url for now)
        var items = new java.util.ArrayList<>();
        paths.forEach((path, item) -> {
            item.readOperationsMap().forEach((method, op) -> {
                Map<String, Object> request = new HashMap<>();
                request.put("method", method.toString());
                Map<String, Object> url = new HashMap<>();
                url.put("raw", "http://localhost:8082" + path);
                url.put("protocol", "http");
                url.put("host", java.util.List.of("localhost"));
                url.put("port", "8082");
                url.put("path", java.util.List.of(path.replaceFirst("^/", "")));
                request.put("url", url);

                Map<String, Object> itemNode = new HashMap<>();
                itemNode.put("name", op.getSummary() != null ? op.getSummary() : method + " " + path);
                itemNode.put("request", request);
                items.add(itemNode);
            });
        });
        collection.put("item", items);

        Path docsDir = Path.of("Docs");
        Files.createDirectories(docsDir);
        File out = new File(docsDir.toFile(), "postman_collection.json");
        try (FileWriter fw = new FileWriter(out)) {
            fw.write(new com.fasterxml.jackson.databind.ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(collection));
        }
    }
}


