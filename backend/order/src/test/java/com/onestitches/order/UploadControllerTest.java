package com.onestitches.order;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UploadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void acceptsValidImage() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "img.png", "image/png", new byte[10]);
        mockMvc.perform(multipart("/api/uploads").file(file))
                .andExpect(status().isOk());
    }

    @Test
    void rejectsUnsupportedType() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "file.txt", "text/plain", new byte[10]);
        mockMvc.perform(multipart("/api/uploads").file(file))
                .andExpect(status().isBadRequest());
    }

    @Test
    void rejectsTooLarge() throws Exception {
        byte[] large = new byte[26 * 1024 * 1024];
        MockMultipartFile file = new MockMultipartFile("file", "img.png", "image/png", large);
        mockMvc.perform(multipart("/api/uploads").file(file))
                .andExpect(status().isPayloadTooLarge());
    }
}
