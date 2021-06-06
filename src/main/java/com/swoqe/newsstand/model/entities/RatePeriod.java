package com.swoqe.newsstand.model.entities;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.time.Period;

@Entity
@Table(name = "periods")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RatePeriod {

    @SequenceGenerator(
            name = "period_sequence",
            sequenceName = "period_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "period_sequence"
    )
    private Long periodId;

    private Period period;

    @NotBlank(message = "Name is mandatory")
    @Size(min = 1, max=255)
    private String formalName;

    @Size(max=255)
    @Column(columnDefinition = "text")
    private String description;

    public RatePeriod(Period period, String formalName, String description) {
        this.period = period;
        this.formalName = formalName;
        this.description = description;
    }
}
