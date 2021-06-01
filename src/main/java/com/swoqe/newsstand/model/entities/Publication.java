package com.swoqe.newsstand.model.entities;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

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
    @Lob
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

    @OneToMany(mappedBy = "publication")
    private List<Genre> genres;


}
