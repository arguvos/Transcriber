package com.arguvos.transcriber;

import com.arguvos.transcriber.service.model.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class HealthcheckTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testHealthcheckEndpoint() throws Exception {
        List<Status> expected = Arrays.asList(new Status("ffmpeg", false),
                new Status("transcribe", false));

        this.mockMvc
                .perform(get("/healthcheck"))
                .andExpect(status().isOk())
                .andExpect(view().name("/healthcheck"))
                .andExpect(model().attributeExists("healthcheck"))
                .andExpect(model().attribute("healthcheck", expected));
    }
}
