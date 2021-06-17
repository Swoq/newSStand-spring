package com.swoqe.newsstand.model.entities;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RateTest {

    @Test
    void compareTo() {
        Publication publication = new Publication(3L, "Title3", "Desc", LocalDate.now(), "Publ", "/path", List.of(), List.of());
        Rate rate = new Rate(1L, new RatePeriod(Period.ZERO, "", ""), publication, BigDecimal.ZERO);

        Publication publication2 = new Publication(3L, "Title3", "Desc", LocalDate.now(), "Publ", "/path", List.of(), List.of());
        Rate rate2 = new Rate(1L, new RatePeriod(Period.ZERO, "", ""), publication2, BigDecimal.ONE);

        int result = rate.compareTo(rate2);

        assertThat(result).isLessThan(0);
    }

    @Test
    void createNewListNullSize() {
        Publication publication = new Publication(3L, "Title3", "Desc", LocalDate.now(), "Publ", "/path", List.of(), List.of());
        List<BigDecimal> prices = List.of(BigDecimal.ONE);
        List<RatePeriod> periods = List.of();

        List<Rate> actual = Rate.createNewList(periods, prices, publication);

        assertThat(actual).isNull();
    }

    @Test
    void createNewList() {
        Publication publication = new Publication(3L, "Title3", "Desc", LocalDate.now(), "Publ", "/path", List.of(), List.of());
        List<BigDecimal> prices = List.of(BigDecimal.ONE);
        List<RatePeriod> periods = List.of(new RatePeriod(1L, Period.ofDays(10), "", ""));

        List<Rate> actual = Rate.createNewList(periods, prices, publication);

        assertThat(actual).isNotNull();
        assertThat(actual.size()).isEqualTo(1);
        Rate rate = actual.get(0);
        assertThat(rate.getRatePeriod()).isEqualTo(periods.get(0));
        assertThat(rate.getPrice()).isEqualTo(prices.get(0));
        assertThat(rate.getPublication()).isEqualTo(publication);
    }

    @Test
    void createNewListExist() {
        RatePeriod period = new RatePeriod(1L, Period.ofDays(10), "", "");
        Publication publication = new Publication(3L, "Title3", "Desc", LocalDate.now(), "Publ", "/path", List.of(), List.of());
        Rate rate = new Rate(1L, period, publication, BigDecimal.TEN);
        publication.setRates(List.of(rate));

        List<BigDecimal> prices = List.of(BigDecimal.ONE);
        List<RatePeriod> periods = List.of(period);

        List<Rate> actual = Rate.createNewList(periods, prices, publication);

        assertThat(actual).isNotNull();
        assertThat(actual.size()).isEqualTo(1);
        Rate result = actual.get(0);
        assertThat(result.getRateId()).isEqualTo(periods.get(0).getPeriodId());
    }
}