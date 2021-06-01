package com.swoqe.newsstand.model.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "genres")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Genre {

    @SequenceGenerator(
            name = "genre_sequence",
            sequenceName = "genre_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "genre_sequence"
    )
    private Long genreId;

    @NotBlank(message = "Name is mandatory")
    @Size(min = 1, max=255)
    private String genreName;

    @Size(max=1000)
    @Lob
    private String description;

    @ManyToOne
    @JoinColumn(name = "publication_id", nullable = false)
    private Publication publication;

    public Genre(String periodName, String description) {
        this.genreName = periodName;
        this.description = description;
    }
}
