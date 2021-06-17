package com.swoqe.newsstand.model.repositories;

import com.swoqe.newsstand.model.entities.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class GenreRepositoryTest {

    @Autowired
    private GenreRepository repository;

    @Test
    @Transactional
    void findAllByGenreIdIn() {
        List<Genre> expected = List.of(new Genre(1L, "genre1", "description1", List.of()),
                new Genre(2L, "genre1", "description1", List.of()),
                new Genre(3L, "genre1", "description1", List.of()),
                new Genre(4L, "genre1", "description1", List.of()));
        List<Long> ids = List.of(1L, 2L, 3L, 4L);

        repository.saveAll(expected);

        List<Genre> received = repository.findAllByGenreIdIn(ids);
        assertThat(expected).isEqualTo(received);
    }
}