package com.swoqe.newsstand.model.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Entity
@Table(name = "subscriptions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Subscription {

    @SequenceGenerator(
            name = "subscription_sequence",
            sequenceName = "subscription_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "subscription_sequence"
    )
    private Long subscriptionId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "rate_id", nullable = false)
    private Rate rate;

    @PastOrPresent
    @NotBlank(message = "StartDate is mandatory")
    private LocalDate startDate;

    @FutureOrPresent
    @NotBlank(message = "EndDate is mandatory")
    private LocalDate endDate;
}
