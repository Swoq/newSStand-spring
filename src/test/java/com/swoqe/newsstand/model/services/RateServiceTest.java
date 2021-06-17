package com.swoqe.newsstand.model.services;

import com.swoqe.newsstand.model.entities.Publication;
import com.swoqe.newsstand.model.entities.Rate;
import com.swoqe.newsstand.model.entities.RatePeriod;
import com.swoqe.newsstand.model.repositories.RateRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SpringBootTest
class RateServiceTest {

    @Captor
    private ArgumentCaptor<ArrayList<Rate>> rateCaptor;

    @Autowired
    private RateService service;

    @MockBean
    private RateRepository repository;

    @Test
    void getRateById() {
        Long id = 1L;
        Publication publication = new Publication(3L, "Title3", "Desc", LocalDate.now(), "Publ", "/path", List.of(), List.of());
        Rate rate = new Rate(1L, new RatePeriod(Period.ZERO, "", ""), publication, BigDecimal.ZERO);

        given(repository.findById(id)).willReturn(Optional.of(rate));

        final Optional<Rate> expected = service.getRateById(id);

        assertThat(expected).isNotNull();
    }

    @Test
    void saveAllRates() {
        Publication publication = new Publication(3L, "Title3", "Desc", LocalDate.now(), "Publ", "/path", List.of(), List.of());
        List<Rate> rates = List.of(
                new Rate(1L, new RatePeriod(Period.ZERO, "", ""), publication, BigDecimal.ZERO),
                new Rate(2L, new RatePeriod(Period.ZERO, "", ""), publication, BigDecimal.ZERO),
                new Rate(3L, new RatePeriod(Period.ZERO, "", ""), publication, BigDecimal.ZERO)
        );

        service.saveAllRates(rates);

        verify(repository).saveAll(rateCaptor.capture());
        List<Rate> capturedRates = rateCaptor.getValue();
        assertThat(capturedRates).isEqualTo(rates);
    }
}