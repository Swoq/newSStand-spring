package com.swoqe.newsstand.model.entities;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "periods")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Period {

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

    @NotBlank(message = "Name is mandatory")
    @Size(min = 1, max=255)
    private String periodName;

    @Size(max=255)
    @Lob
    private String description;

    public Period(String periodName, String description) {
        this.periodName = periodName;
        this.description = description;
    }
}
