package com.arguvos.transcriber;

import com.arguvos.transcriber.repository.RecognizeRepository;
import com.arguvos.transcriber.repository.UserRepository;
import com.arguvos.transcriber.service.HistoryService;
import com.arguvos.transcriber.utils.Helper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class HistoryTests {

    @Autowired
    HistoryService historyService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RecognizeRepository recognizeRepository;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testHistoryEndpoint() throws Exception {
        String username = UUID.randomUUID().toString();
        userRepository.save(Helper.createUser(username));
        Long userId = userRepository.findByUsername(username).getId();

        recognizeRepository.save(Helper.createRecord(userId));
        recognizeRepository.save(Helper.createRecord(userId));
        recognizeRepository.save(Helper.createRecord(userId));

        this.mockMvc
                .perform(get("/history").with(user(username)))
                .andExpect(status().isOk())
                .andExpect(view().name("/history"))
                .andExpect(model().attributeExists("history"));
    }
}
