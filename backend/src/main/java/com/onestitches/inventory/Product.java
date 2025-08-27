package com.onestitches.inventory;

/**
 * Simple product representation used for catalog stubs.
 */
public record Product(String sku, String name, String category, double price, String imageUrl, int stock) {}
