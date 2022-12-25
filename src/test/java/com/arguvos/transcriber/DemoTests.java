package com.arguvos.transcriber;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(properties = { "demo.max-file-size=1" })
public class DemoTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testDemoEndpoint() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
        this.mockMvc
                .perform(multipart("/demo").file(file))
                .andExpect(status().isOk())
                .andExpect(view().name("/record"))
                .andExpect(model().attribute("record", hasProperty("originalFileName", is(file.getOriginalFilename()))))
                .andExpect(model().attribute("record", hasProperty("fileSize", is(file.getSize()))))
                .andExpect(model().attribute("record", hasProperty("storedFileName", notNullValue())))
                .andExpect(model().attribute("record", hasProperty("status", notNullValue())))
                .andExpect(model().attribute("record", hasProperty("progressStep", notNullValue())))
                .andExpect(model().attribute("record", hasProperty("createDate", notNullValue())));
    }

    @Test
    void testMaxFileSizeAtDemoEndpoint() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                new byte[2000000] //2Mb
        );
        this.mockMvc
                .perform(multipart("/demo").file(file))
                .andExpect(status().isOk())
                .andExpect(view().name("/index"));
    }
}
