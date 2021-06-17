package com.swoqe.newsstand.model.entities;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "rates")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Rate implements Comparable<Rate> {

    @SequenceGenerator(
            name = "rate_sequence",
            sequenceName = "rate_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "rate_sequence"
    )
    private Long rateId;

    @ManyToOne
    @JoinColumn(name = "period_id", nullable = false)
    private RatePeriod ratePeriod;

    @ManyToOne
    @JoinColumn(name = "publication_id", nullable = false)
    private Publication publication;

    @Positive
    private BigDecimal price;

    public Rate(RatePeriod ratePeriod, Publication publication, BigDecimal price) {
        this.ratePeriod = ratePeriod;
        this.publication = publication;
        this.price = price;
    }

    @Override
    public int compareTo(@NotNull Rate o) {
        return price.compareTo(o.price);
    }

    public static List<Rate> createNewList(List<RatePeriod> periods, List<BigDecimal> prices, Publication publication) {
        if (periods.size() != prices.size())
            return null;
        List<Rate> rates = new ArrayList<>();
        for (int i = 0; i < periods.size(); i++) {
            RatePeriod p = periods.get(i);
            Optional<Rate> existingRate = Optional.empty();
            if (publication.getRates() != null)
                existingRate = publication.getRates().stream()
                        .filter((s) -> s.getRatePeriod().equals(p)).findAny();
            if (existingRate.isPresent()) {
                Rate rate = existingRate.get();
                rates.add(new Rate(rate.getRateId(), p, publication, prices.get(i)));
            } else
                rates.add(new Rate(p, publication, prices.get(i)));
        }
        return rates;
    }
}
