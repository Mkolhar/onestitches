package com.onestitches.order;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UploadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void presignAndUpload() throws Exception {
        String json = mockMvc.perform(post("/api/uploads/presign").param("fileName", "img.png"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(json);
        String url = node.get("url").asText();

        MockMultipartFile file = new MockMultipartFile("file", "img.png", "image/png", new byte[10]);
        mockMvc.perform(multipart(url).file(file).with(req -> {req.setMethod("PUT"); return req;}))
                .andExpect(status().isCreated());
    }

    @Test
    void rejectsUnsupportedType() throws Exception {
        String url = objectMapper.readTree(mockMvc.perform(post("/api/uploads/presign").param("fileName", "img.png"))
                .andReturn().getResponse().getContentAsString()).get("url").asText();
        MockMultipartFile file = new MockMultipartFile("file", "file.txt", "text/plain", new byte[10]);
        mockMvc.perform(multipart(url).file(file).with(req -> {req.setMethod("PUT"); return req;}))
                .andExpect(status().isBadRequest());
    }

    @Test
    void rejectsTooLarge() throws Exception {
        String url = objectMapper.readTree(mockMvc.perform(post("/api/uploads/presign").param("fileName", "img.png"))
                .andReturn().getResponse().getContentAsString()).get("url").asText();
        byte[] large = new byte[26 * 1024 * 1024];
        MockMultipartFile file = new MockMultipartFile("file", "img.png", "image/png", large);
        mockMvc.perform(multipart(url).file(file).with(req -> {req.setMethod("PUT"); return req;}))
                .andExpect(status().isPayloadTooLarge());
    }

    @Test
    void rejectsUnknownToken() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "img.png", "image/png", new byte[10]);
        mockMvc.perform(multipart("/api/uploads/badtoken").file(file).with(req -> {req.setMethod("PUT"); return req;}))
                .andExpect(status().isNotFound());
    }
}
