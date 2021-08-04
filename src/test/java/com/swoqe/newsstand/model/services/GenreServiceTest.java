package com.swoqe.newsstand.model.services;

import com.swoqe.newsstand.model.entities.Genre;
import com.swoqe.newsstand.model.repositories.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
class GenreServiceTest {

    @Autowired
    private GenreService genreService;

    @MockBean
    private GenreRepository genreRepository;

    @Test
    void getAllGenres() {
        List<Genre> expect = List.of(
                new Genre("genre1", "description1"),
                new Genre("genre2", "description2"),
                new Genre("genre3", "description3"),
                new Genre("genre4", "description4")
        );
        doReturn(expect).when(genreRepository).findAll();

        List<Genre> received = genreService.findAll();
        Assertions.assertEquals(4, received.size(), "getAllGenres should return 4 genres");
    }

    @Test
    void getAllGenresByIds() {
        List<Genre> expected = List.of(
                new Genre(1L, "genre1", "description1", List.of()),
                new Genre(2L, "genre1", "description1", List.of()),
                new Genre(3L, "genre1", "description1", List.of()),
                new Genre(4L, "genre1", "description1", List.of())
        );
        List<Long> longs = List.of(1L, 2L, 3L, 4L);
        doReturn(expected).when(genreRepository).findAllByGenreIdIn(longs);

        List<Genre> received = genreService.findAllByIds(longs);
        Assertions.assertEquals(4, received.size(), "getAllGenresByIds should return 4 genres");
    }

    @Test
    void addNewGenre() {
        Genre genre = new Genre(2L, "genre1", "description1", List.of());

        ArgumentCaptor<Genre> genreArgumentCaptor = ArgumentCaptor.forClass(Genre.class);
        doReturn(genre).when(genreRepository).save(genreArgumentCaptor.capture());
        genreService.save(genre);

        Genre capturedGenre = genreArgumentCaptor.getValue();
        assertThat(capturedGenre).isEqualTo(genre);
    }
}