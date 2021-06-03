package com.swoqe.newsstand.model.entities;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "publications")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Publication {

    @SequenceGenerator(
            name = "publication_sequence",
            sequenceName = "publication_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "publication_sequence"
    )
    private Long publicationId;

    @NotBlank(message = "Title is mandatory")
    @Size(min = 1, max=255)
    private String title;

    @Size(max=1000)
    @Column(columnDefinition = "text")
    private String description;

    @NotBlank(message = "Publication Date is mandatory")
    @PastOrPresent
    @DateTimeFormat()
    private LocalDate publicationDate;

    @NotBlank(message = "Publisher Name is mandatory")
    @Size(min = 1, max=255)
    private String publisherName;

    @Pattern(regexp = "^(.+)\\/([^\\/]+)$")
    private String imagePath;

    @OneToMany(mappedBy = "publication")
    private List<Rate> rates;

    @ManyToMany
    @JoinTable(
            name = "publications_genres",
            joinColumns = @JoinColumn(name = "publication_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private List<Genre> genres;

    public Rate getShownRate(String param){
        if(this.rates.isEmpty())
            return new Rate(new Period("", ""), this, BigDecimal.ZERO);

        switch (param){
            case "min":
                return Collections.min(rates);
            case "max":
                return Collections.max(rates);
            default:
                break;
        }
        return Collections.min(rates);
    }

}
