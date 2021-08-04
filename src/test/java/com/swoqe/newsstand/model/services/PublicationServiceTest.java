package com.swoqe.newsstand.model.services;

import com.swoqe.newsstand.model.entities.Genre;
import com.swoqe.newsstand.model.entities.Publication;
import com.swoqe.newsstand.model.entities.Rate;
import com.swoqe.newsstand.model.entities.RatePeriod;
import com.swoqe.newsstand.model.repositories.PublicationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@SpringBootTest
class PublicationServiceTest {

    private List<Publication> publications;

    @Captor
    private ArgumentCaptor<ArrayList<Genre>> genreCaptor;

    @Captor
    private ArgumentCaptor<ArrayList<Rate>> rateCaptor;

    @Autowired
    private PublicationService service;

    @MockBean
    private PublicationRepository repository;

    @MockBean
    private RateService rateService;

    @BeforeEach
    void init() {
        publications = List.of(
                new Publication(1L, "Title1", "Desc", LocalDate.now(), "Publ", "/path", List.of(), List.of()),
                new Publication(2L, "Title2", "Desc", LocalDate.now(), "Publ", "/path", List.of(), List.of()),
                new Publication(3L, "Title3", "Desc", LocalDate.now(), "Publ", "/path", List.of(), List.of()),
                new Publication(4L, "Title4", "Desc", LocalDate.now(), "Publ", "/path", List.of(), List.of())
        );
    }

    @Test
    void getAllPublications() {
        Page<Publication> page = new PageImpl<>(publications);
        Pageable paging = PageRequest.of(0, 5, Sort.unsorted());
        doReturn(page).when(repository).findAllBy(paging);

        Page<Publication> received = service.findAll(paging);

        Assertions.assertEquals(4, received.getContent().size(), "getAllPublications should return 4 genres");
    }

    @Test
    void getAllPublicationsByName() {
        Page<Publication> page = new PageImpl<>(publications);
        Pageable paging = PageRequest.of(0, 5, Sort.unsorted());

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Pageable> pageableArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);
        doReturn(page).when(repository).
                findAllByTitleContainsIgnoreCase(stringArgumentCaptor.capture(), pageableArgumentCaptor.capture());
        Page<Publication> allPublicationsByName = service.findAllByTitle("", paging);

        Pageable captured = pageableArgumentCaptor.getValue();
        assertThat(captured).isEqualTo(paging);
        assertThat(allPublicationsByName.getTotalElements()).isEqualTo(publications.size());
    }

    @Test
    void getAllPublicationsByGenres() {
        Pageable paging = PageRequest.of(0, 5, Sort.unsorted());

        List<Genre> genres = List.of(new Genre("genre1", "description1"),
                new Genre("genre2", "description2"),
                new Genre("genre3", "description3"),
                new Genre("genre4", "description4"));

        service.findAllByGenres(genres, paging);

        verify(repository).findAllByGenresIn(genreCaptor.capture(), eq(paging));
        List<Genre> captured = genreCaptor.getValue();

        assertThat(captured.size()).isEqualTo(genres.size());
        assertThat(genres).isEqualTo(captured);

    }

    @Test
    void getPublicationById() {
        Publication publication = new Publication(3L, "Title3", "Desc", LocalDate.now(), "Publ", "/path", List.of(), List.of());
        Long id = 1L;
        doReturn(Optional.of(publication)).when(repository).findFirstByPublicationId(id);
        Publication received = service.findById(id).orElse(null);

        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(repository).findFirstByPublicationId(longArgumentCaptor.capture());

        Long captured = longArgumentCaptor.getValue();
        assertThat(captured).isEqualTo(id);
        assertThat(received).isEqualTo(publication);
    }

    @Test
    void savePublicationWithRates() {
        Publication publication = new Publication(3L, "Title3", "Desc", LocalDate.now(), "Publ", "/path", List.of(), List.of());
        List<Rate> rates = List.of(
                new Rate(1L, new RatePeriod(Period.ZERO, "", ""), publication, BigDecimal.ZERO),
                new Rate(2L, new RatePeriod(Period.ZERO, "", ""), publication, BigDecimal.ZERO),
                new Rate(3L, new RatePeriod(Period.ZERO, "", ""), publication, BigDecimal.ZERO)
        );

        service.savePublicationWithRates(publication, rates);

        ArgumentCaptor<Publication> publicationArgumentCaptor = ArgumentCaptor.forClass(Publication.class);
        verify(repository).save(publicationArgumentCaptor.capture());
        verify(rateService).saveAll(rateCaptor.capture());

        Publication capturedPublication = publicationArgumentCaptor.getValue();
        List<Rate> capturedRates = rateCaptor.getValue();
        assertThat(capturedPublication).isEqualTo(publication);
        assertThat(capturedRates).isEqualTo(rates);
    }
}