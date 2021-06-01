package com.swoqe.newsstand.model.entities;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Entity
@Table(name = "rates")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Rate implements Comparable<Rate>{

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
    @JoinColumn(name="period_id", nullable=false)
    private Period period;

    @ManyToOne
    @JoinColumn(name="publication_id", nullable=false)
    private Publication publication;

    @Positive
    private BigDecimal price;

    public Rate(Period period, Publication publication, BigDecimal price) {
        this.period = period;
        this.publication = publication;
        this.price = price;
    }

    @Override
    public int compareTo(@NotNull Rate o) {
        return price.compareTo(o.price);
    }
}
