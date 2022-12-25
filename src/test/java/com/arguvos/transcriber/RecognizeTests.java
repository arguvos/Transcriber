package com.arguvos.transcriber;

import com.arguvos.transcriber.model.Record;
import com.arguvos.transcriber.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class RecognizeTests {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @WithMockUser
    void testRecognizeEndpoint() throws Exception {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
        this.mockMvc
                .perform(multipart("/recognize").file(file))
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
    @WithMockUser
    void testGetRecordEndpoint() throws Exception {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
        Record record = (Record) Objects.requireNonNull(this.mockMvc
                .perform(multipart("/recognize").file(file))
                .andExpect(status().isOk())
                .andReturn().getModelAndView()).getModel().get("record");
        this.mockMvc
                .perform(get("/recognize/" + record.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("/record"))
                .andExpect(model().attribute("record", hasProperty("originalFileName", is(file.getOriginalFilename()))))
                .andExpect(model().attribute("record", hasProperty("fileSize", is(file.getSize()))))
                .andExpect(model().attribute("record", hasProperty("storedFileName", notNullValue())))
                .andExpect(model().attribute("record", hasProperty("status", notNullValue())))
                .andExpect(model().attribute("record", hasProperty("progressStep", notNullValue())))
                .andExpect(model().attribute("record", hasProperty("createDate", notNullValue())));

    }
}
