package com.arguvos.transcriber;

import com.arguvos.transcriber.model.UserEntity;
import com.arguvos.transcriber.repository.UserRepository;
import com.arguvos.transcriber.service.model.UserData;
import com.arguvos.transcriber.utils.Helper;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class ProfileTests {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void testGetProfileEndpoint() throws Exception {
        String username = UUID.randomUUID().toString();
        UserEntity user = Helper.createUser(username);
        userRepository.save(user);

        UserData userData = UserData.builder().username(user.getUsername())
                .email(user.getEmail()).build();

        this.mockMvc
                .perform(get("/profile").with(user(username)))
                .andExpect(status().isOk())
                .andExpect(view().name("/profile"))
                .andExpect(model().attributeExists("userData"))
                .andExpect(model().attribute("userData", userData));
    }

    @Test
    void testUpdateProfileEndpoint() throws Exception {
        String username = UUID.randomUUID().toString();
        UserEntity user = Helper.createUser(username);
        userRepository.save(user);

        String newPassword = "new_password";
        String newEmail = "new@mail.com";

        UserData userData = UserData.builder().username(user.getUsername())
                .email(newEmail).build();

        this.mockMvc
                .perform(post("/profile").with(user(username)).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
                                new BasicNameValuePair("password", newPassword),
                                new BasicNameValuePair("email", newEmail)
                        )))))
                .andExpect(status().isOk())
                .andExpect(view().name("/profile"))
                .andExpect(model().attributeExists("userData"))
                .andExpect(model().attribute("userData", userData));
    }
}
