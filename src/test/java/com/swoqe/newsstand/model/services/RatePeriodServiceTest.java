package com.swoqe.newsstand.model.services;

import com.swoqe.newsstand.model.entities.RatePeriod;
import com.swoqe.newsstand.model.repositories.PeriodRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Period;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SpringBootTest
class RatePeriodServiceTest {

    @Autowired
    private RatePeriodService service;

    @MockBean
    private PeriodRepository repository;

    @Test
    void getOneById() {
        final Long id = 1L;
        final RatePeriod given = new RatePeriod(1L, Period.ZERO, "", "");
        given(repository.findById(id)).willReturn(Optional.of(given));

        final Optional<RatePeriod> expected = service.getOneById(id);
        assertThat(expected).isNotNull();
    }

    @Test
    void addNewRatePeriod() {
        final RatePeriod given = new RatePeriod(1L, Period.ZERO, "", "");
        given(repository.save(given)).willAnswer(invocation -> invocation.getArgument(0));

        service.addNewRatePeriod(given);

        ArgumentCaptor<RatePeriod> ratePeriodArgumentCaptor = ArgumentCaptor.forClass(RatePeriod.class);
        verify(repository).save(ratePeriodArgumentCaptor.capture());

        RatePeriod captured = ratePeriodArgumentCaptor.getValue();
        assertThat(captured).isEqualTo(given);
    }

    @Test
    void getAllRatePeriods() {
        List<RatePeriod> given = List.of(
                new RatePeriod(1L, Period.ZERO, "", ""),
                new RatePeriod(2L, Period.ZERO, "", ""),
                new RatePeriod(3L, Period.ZERO, "", ""),
                new RatePeriod(4L, Period.ZERO, "", ""));

        given(repository.findAll()).willReturn(given);

        List<RatePeriod> received = service.getAllRatePeriods();

        assertThat(received).isEqualTo(given);
    }
}