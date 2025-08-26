package com.onestitches.inventory;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private static final Map<String, Product> PRODUCTS = Map.of(
            "TSHIRT-001", new Product("TSHIRT-001", "T-Shirt", "apparel", 499.0, "https://placehold.co/600x400"),
            "HOODIE-001", new Product("HOODIE-001", "Hoodie", "outerwear", 999.0, "https://placehold.co/600x400"),
            "MUG-001", new Product("MUG-001", "Coffee Mug", "merch", 299.0, "https://placehold.co/600x400"));

    @GetMapping("/products")
    public List<Product> list(@RequestParam(required = false) String category) {
        return PRODUCTS.values().stream()
                .filter(p -> category == null || p.category().equalsIgnoreCase(category))
                .toList();
    }

    @GetMapping("/products/{sku}")
    public Product get(@PathVariable String sku) {
        Product product = PRODUCTS.get(sku);
        if (product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return product;
    }
}
