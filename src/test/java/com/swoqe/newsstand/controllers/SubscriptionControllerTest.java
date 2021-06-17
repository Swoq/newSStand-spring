package com.swoqe.newsstand.controllers;

import com.swoqe.newsstand.model.entities.*;
import com.swoqe.newsstand.model.services.RateService;
import com.swoqe.newsstand.model.services.SubscriptionService;
import com.swoqe.newsstand.model.services.UserService;
import com.swoqe.newsstand.security.entity.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SubscriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private RateService rateService;

    @MockBean
    private SubscriptionService subscriptionService;

    private User user;
    private Publication publication;
    private Rate rate;
    private Subscription subscription;

    @BeforeEach
    public void init(){
        user = new User("f", "f", "fas", UserRole.COMMON_USER, "email@gmailc.com");
        publication = new Publication(3L, "Title3", "Desc", LocalDate.now(), "Publ", "/path", List.of(), List.of());
        rate = new Rate(1L, new RatePeriod(Period.ZERO, "", ""), publication, BigDecimal.ZERO);
    }

    @Test
    @WithMockUser(value = "common", authorities = "COMMON_USER")
    @DisplayName("POST /user/subscriptions/add")
    void getAccountPage() throws Exception{
        user.setId(1L);
        user.setSubscriptions(List.of());
        String message = "You have successfully subscribed";

        given(userService.getUserById(any())).willReturn(Optional.of(user));
        given(userService.doSubscription(user, rate)).willReturn(message);
        given(rateService.getRateById(rate.getRateId())).willReturn(Optional.of(rate));

        mockMvc.perform(post("/user/subscriptions/add")
                .param("rateId", String.valueOf(rate.getRateId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("information", message));
    }

    @Test
    @WithMockUser(value = "common", authorities = "COMMON_USER")
    @DisplayName("POST /user/subscriptions/add FORBIDDEN")
    void shouldReturnForbidden() throws Exception{
        given(userService.getUserById(any())).willReturn(Optional.empty());
        MediaType textHtml = new MediaType(MediaType.TEXT_HTML, StandardCharsets.UTF_8);
        mockMvc.perform(post("/user/subscriptions/add")
                .param("rateId", String.valueOf(rate.getRateId())))
                .andExpect(status().isOk())
                .andExpect(model().attribute("statusCode", 403))
                .andExpect(content().contentType(MediaType.valueOf(String.valueOf(textHtml))));
    }

    @Test
    @WithMockUser(value = "common", authorities = "COMMON_USER")
    @DisplayName("POST /user/subscriptions/add NOT_FOUND")
    void shouldReturnNotFound() throws Exception{
        given(rateService.getRateById(any())).willReturn(Optional.empty());
        given(userService.getUserById(user.getId())).willReturn(Optional.of(user));
        MediaType textHtml = new MediaType(MediaType.TEXT_HTML, StandardCharsets.UTF_8);
        mockMvc.perform(post("/user/subscriptions/add")
                .param("rateId", String.valueOf(rate.getRateId())))
                .andExpect(status().isOk())
                .andExpect(model().attribute("statusCode", 404))
                .andExpect(content().contentType(MediaType.valueOf(String.valueOf(textHtml))));
    }

    @Test
    @WithMockUser(value = "common", authorities = "COMMON_USER")
    @DisplayName("POST /user/subscriptions/add ALREADY SUBSCRIBE")
    void hasAlreadySubscribed() throws Exception{
        given(userService.getUserById(user.getId())).willReturn(Optional.of(user));
        given(rateService.getRateById(any())).willReturn(Optional.of(this.rate));
        subscription = new Subscription(1L, user, rate, LocalDate.now(), LocalDate.now());
        user.setSubscriptions(List.of(subscription));
        String expected = "You have already subscribed to this publication!";

        mockMvc.perform(post("/user/subscriptions/add")
                .param("rateId", String.valueOf(rate.getRateId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("information", expected));
    }

    @BeforeEach
    public void beforeEachCancel(){
        subscription = new Subscription(1L, user, rate, LocalDate.now(), LocalDate.now());
    }

    @Test
    @WithMockUser(value = "common", authorities = "COMMON_USER")
    @DisplayName("POST /user/subscriptions/cancel")
    void shouldCancerSubscription() throws Exception{
        user.setId(1L);
        user.setSubscriptions(List.of());
        String expected = "You have successfully unsubscribed!";

        given(userService.getUserById(any())).willReturn(Optional.of(user));
        given(userService.cancelSubscription(user, subscription)).willReturn(expected);
        given(subscriptionService.getSubscriptionById(subscription.getSubscriptionId()))
                .willReturn(Optional.of(subscription));

        mockMvc.perform(post("/user/subscriptions/cancel")
                .param("subscriptionId", String.valueOf(subscription.getSubscriptionId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("information", expected));
    }

    @Test
    @WithMockUser(value = "common", authorities = "COMMON_USER")
    @DisplayName("POST /user/subscriptions/cancel FORBIDDEN")
    void shouldReturnForbiddenWithoutUser() throws Exception{
        given(userService.getUserById(any())).willReturn(Optional.empty());
        MediaType textHtml = new MediaType(MediaType.TEXT_HTML, StandardCharsets.UTF_8);
        mockMvc.perform(post("/user/subscriptions/cancel")
                .param("subscriptionId", String.valueOf(subscription.getSubscriptionId())))
                .andExpect(status().isOk())
                .andExpect(model().attribute("statusCode", 403))
                .andExpect(content().contentType(MediaType.valueOf(String.valueOf(textHtml))));
    }

    @Test
    @WithMockUser(value = "common", authorities = "COMMON_USER")
    @DisplayName("POST /user/subscriptions/add NOT_FOUND")
    void shouldReturnNotFoundSubscription() throws Exception{
        given(subscriptionService.getSubscriptionById(any())).willReturn(Optional.empty());
        given(userService.getUserById(user.getId())).willReturn(Optional.of(user));
        MediaType textHtml = new MediaType(MediaType.TEXT_HTML, StandardCharsets.UTF_8);
        mockMvc.perform(post("/user/subscriptions/cancel")
                .param("subscriptionId", String.valueOf(subscription.getSubscriptionId())))
                .andExpect(status().isOk())
                .andExpect(model().attribute("statusCode", 404))
                .andExpect(content().contentType(MediaType.valueOf(String.valueOf(textHtml))));
    }

    @Test
    @WithMockUser(value = "common", authorities = "COMMON_USER")
    @DisplayName("POST /user/subscriptions/add ALREADY SUBSCRIBE")
    void userUnBindWithSubscription() throws Exception{
        User user2 = new User("", "", "", UserRole.COMMON_USER, "");
        subscription.setUser(user2);

        given(userService.getUserById(this.user.getId())).willReturn(Optional.of(this.user));
        given(subscriptionService.getSubscriptionById(any())).willReturn(Optional.of(subscription));
        given(userService.cancelSubscription(user, subscription)).willThrow(new ResponseStatusException(NOT_FOUND));

        mockMvc.perform(post("/user/subscriptions/cancel")
                .param("subscriptionId", String.valueOf(subscription.getSubscriptionId())))
                .andExpect(status().isOk())
                .andExpect(model().attribute("statusCode", 404));
    }
}