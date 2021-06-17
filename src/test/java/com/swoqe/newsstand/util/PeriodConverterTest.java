package com.swoqe.newsstand.util;

import org.junit.jupiter.api.Test;

import java.time.Period;

import static org.assertj.core.api.Assertions.assertThat;

class PeriodConverterTest {

    @Test
    void convertToDatabaseColumn() {
        Period input = Period.ofDays(10);
        PeriodConverter periodConverter = new PeriodConverter();
        Long received = periodConverter.convertToDatabaseColumn(input);

        assertThat(Long.valueOf(input.getDays())).isEqualTo(received);
    }

    @Test
    void convertToEntityAttribute() {
        long input = 10L;
        PeriodConverter periodConverter = new PeriodConverter();
        Period received = periodConverter.convertToEntityAttribute(input);

        assertThat(received).isEqualTo(Period.ofDays(Math.toIntExact(input)));
    }
}