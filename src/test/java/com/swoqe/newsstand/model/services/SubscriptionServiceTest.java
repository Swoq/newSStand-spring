package com.swoqe.newsstand.model.services;

import com.swoqe.newsstand.model.entities.*;
import com.swoqe.newsstand.model.repositories.SubscriptionRepository;
import com.swoqe.newsstand.security.entity.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class SubscriptionServiceTest {

    @Autowired
    private SubscriptionService service;

    @MockBean
    private SubscriptionRepository repository;

    @Test
    void getSubscriptionById() {
        final Publication publication = new Publication(3L, "Title3", "Desc", LocalDate.now(), "Publ", "/path", List.of(), List.of());
        final User user = new User("f", "f", "fas", UserRole.COMMON_USER, "email@gmailc.com");
        final Rate rate = new Rate(1L, new RatePeriod(Period.ZERO, "", ""), publication, BigDecimal.ZERO);
        final Subscription subscription = new Subscription(user, rate, LocalDate.now(), LocalDate.now());

        given(repository.findById(1L)).willReturn(Optional.of(subscription));

        Optional<Subscription> received = service.findById(1L);

        assertThat(received).isNotNull();
    }
}