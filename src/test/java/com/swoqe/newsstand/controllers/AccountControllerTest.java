package com.swoqe.newsstand.controllers;

import com.swoqe.newsstand.model.entities.User;
import com.swoqe.newsstand.model.services.UserService;
import com.swoqe.newsstand.security.entity.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(value = "common", authorities = "COMMON_USER")
    @DisplayName("GET /user/account")
    void getAccountPage() throws Exception {
        User user = new User("f", "f", "fas", UserRole.COMMON_USER, "email@gmailc.com");
        given(userService.getUserById(any())).willReturn(Optional.of(user));
        MediaType textHtml = new MediaType(MediaType.TEXT_HTML, StandardCharsets.UTF_8);
        mockMvc.perform(get("/user/account").flashAttr("information", ""))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf(String.valueOf(textHtml))));
    }

    @Test
    @WithMockUser(value = "common", authorities = "COMMON_USER")
    @DisplayName("GET /user/account THROW")
    void shouldThrowNotFound() throws Exception {
        given(userService.getUserById(any())).willReturn(Optional.empty());
        MediaType textHtml = new MediaType(MediaType.TEXT_HTML, StandardCharsets.UTF_8);
        mockMvc.perform(get("/user/account"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("statusCode", 403))
                .andExpect(content().contentType(MediaType.valueOf(String.valueOf(textHtml))));
    }

    @Test
    @DisplayName("GET /user/account REDIRECT")
    void noAccess() throws Exception {
        mockMvc.perform(get("/user/account"))
                .andExpect(status().isFound());
    }
}