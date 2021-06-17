package com.swoqe.newsstand.model.services;

import com.swoqe.newsstand.model.entities.*;
import com.swoqe.newsstand.model.repositories.SubscriptionRepository;
import com.swoqe.newsstand.model.repositories.UserRepository;
import com.swoqe.newsstand.security.entity.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@SpringBootTest
class UserServiceTest {

    private User user;
    private Publication publication;
    private Rate rate;
    private Subscription subscription;

    @Autowired
    private UserService service;

    @MockBean
    private UserRepository repository;

    @MockBean
    private SubscriptionRepository subscriptionRepository;

    @BeforeEach
    private void init() {
        user = new User("f", "f", "fas", UserRole.COMMON_USER, "email@gmailc.com");
        publication = new Publication(3L, "Title3", "Desc", LocalDate.now(), "Publ", "/path", List.of(), List.of());
        rate = new Rate(1L, new RatePeriod(Period.ZERO, "", ""), publication, BigDecimal.valueOf(10L));
        subscription = new Subscription(user, rate, LocalDate.now(), LocalDate.now());
    }

    @Test
    void signUpUser() {
        given(repository.save(user)).willAnswer(invocation -> invocation.getArgument(0));

        service.signUpUser(user);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(repository).save(userArgumentCaptor.capture());

        User captured = userArgumentCaptor.getValue();
        assertThat(captured).isEqualTo(user);
    }

    @Test
    void getUserById() {
        final Long id = 1L;

        given(repository.findById(id)).willReturn(Optional.of(user));

        final Optional<User> expected = service.getUserById(id);
        assertThat(expected).isNotNull();
    }

    @Test
    void shouldMinusAccount() {
        BigDecimal initialAccount = BigDecimal.valueOf(100L);
        BigDecimal expectedAccount = initialAccount.subtract(rate.getPrice());
        user.setAccount(initialAccount);

        service.doSubscription(user, rate);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(repository).save(userArgumentCaptor.capture());

        User captured = userArgumentCaptor.getValue();
        assertThat(captured.getAccount()).isEqualTo(expectedAccount);
    }

    @Test
    void shouldReturnNotEnoughMoneyString() {
        BigDecimal initialAccount = BigDecimal.valueOf(100L);
        rate.setPrice(BigDecimal.valueOf(10L));
        user.setAccount(initialAccount);

        String returned = service.doSubscription(user, rate);

        String expected = "You have successfully subscribed";
        assertThat(returned).isEqualTo(expected);
    }

    @Test
    void shouldSuccessfullySubscribe() {
        BigDecimal initialAccount = BigDecimal.valueOf(100L);
        rate.setPrice(BigDecimal.valueOf(150L));
        user.setAccount(initialAccount);

        String returned = service.doSubscription(user, rate);

        String expected = "You don't have enough money to subscribe";
        assertThat(returned).isEqualTo(expected);
    }

    @Test
    void shouldCancelSubscription() {
        String expected = "You have successfully unsubscribed!";
        String returned = service.cancelSubscription(user, subscription);
        assertThat(expected).isEqualTo(returned);
    }

    @Test
    void shouldSetEndDateNow() {
        LocalDate expected = LocalDate.now();
        service.cancelSubscription(user, subscription);

        ArgumentCaptor<Subscription> subscriptionArgumentCaptor = ArgumentCaptor.forClass(Subscription.class);
        verify(subscriptionRepository).save(subscriptionArgumentCaptor.capture());

        Subscription captured = subscriptionArgumentCaptor.getValue();
        assertThat(captured.getEndDate()).isEqualTo(expected);
    }

    @Test
    void shouldThrowWhenUserDontHaveSubscription() {
        User user2 = new User("f", "f", "fas", UserRole.COMMON_USER, "email@gmailc.com");
        user2.setId(2L);

        assertThrows(ResponseStatusException.class, () -> service.cancelSubscription(user2, subscription));

        verify(subscriptionRepository, never()).save(any(Subscription.class));
    }

    @Test
    void shouldSuccessfullyBlockUser() {
        String email = "email@gmailc.com";
        String expected = "User has been successfully blocked!";
        given(repository.findByEmail(email)).willReturn(Optional.of(user));
        String returned = service.blockUserByEmail(email);
        assertThat(returned).isEqualTo(expected);
    }

    @Test
    void shouldNotBlockUserWithNoEmail() {
        String email = "test";
        String expected = "Unable to find user with email: test";
        given(repository.findByEmail(email)).willReturn(Optional.empty());
        String returned = service.blockUserByEmail(email);
        assertThat(returned).isEqualTo(expected);
    }

    @Test
    void shouldSetBlockedTrue() {
        String email = "email@gmailc.com";
        given(repository.findByEmail(email)).willReturn(Optional.of(user));
        service.blockUserByEmail(email);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(repository).save(userArgumentCaptor.capture());

        User captured = userArgumentCaptor.getValue();
        assertThat(captured.isLocked()).isTrue();
    }

    @Test
    void shouldSuccessfullyUnBlockUser() {
        String email = "email@gmailc.com";
        String expected = "User has been successfully unblocked!";
        given(repository.findByEmail(email)).willReturn(Optional.of(user));
        String returned = service.unblockUserByEmail(email);
        assertThat(returned).isEqualTo(expected);
    }

    @Test
    void shouldNotUnBlockUserWithNoEmail() {
        String email = "test";
        String expected = "Unable to find user with email: test";
        given(repository.findByEmail(email)).willReturn(Optional.empty());
        String returned = service.unblockUserByEmail(email);
        assertThat(returned).isEqualTo(expected);
    }

    @Test
    void shouldSetBlockedFalse() {
        String email = "email@gmailc.com";
        given(repository.findByEmail(email)).willReturn(Optional.of(user));
        service.unblockUserByEmail(email);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(repository).save(userArgumentCaptor.capture());

        User captured = userArgumentCaptor.getValue();
        assertThat(captured.isLocked()).isFalse();
    }
}