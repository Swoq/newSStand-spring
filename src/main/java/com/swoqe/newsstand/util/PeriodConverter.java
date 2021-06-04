package com.swoqe.newsstand.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.Duration;
import java.time.Period;

@Converter(autoApply = true)
public class PeriodConverter implements AttributeConverter<Period, Long> {

    @Override
    public Long convertToDatabaseColumn(Period period) {
        return (long) period.getDays();
    }

    @Override
    public Period convertToEntityAttribute(Long aLong) {
        return Period.ofDays(Math.toIntExact(aLong));
    }
}