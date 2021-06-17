package com.swoqe.newsstand.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderByTest {

    @Test
    void safeValueOf() {
        String input = "invalid";
        OrderBy expected = OrderBy.title;
        OrderBy received = OrderBy.safeValueOf(input);

        assertThat(expected).isEqualTo(received);
    }
}