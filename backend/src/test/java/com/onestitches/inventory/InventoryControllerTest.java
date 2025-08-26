package com.onestitches.inventory;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void productsEndpointReturnsOk() throws Exception {
        mockMvc.perform(get("/api/inventory/products")).andExpect(status().isOk());
    }

    @Test
    void productsCanBeFilteredByCategory() throws Exception {
        mockMvc.perform(get("/api/inventory/products").param("category", "apparel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].category").value("apparel"));
    }

    @Test
    void productDetailReturnsItem() throws Exception {
        mockMvc.perform(get("/api/inventory/products/TSHIRT-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sku").value("TSHIRT-001"));
    }

    @Test
    void productDetailNotFoundReturns404() throws Exception {
        mockMvc.perform(get("/api/inventory/products/UNKNOWN"))
                .andExpect(status().isNotFound());
    }
}
