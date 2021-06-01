package com.swoqe.newsstand.model.services;

import com.swoqe.newsstand.model.entities.Genre;
import com.swoqe.newsstand.model.repositories.GenreRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;

    public List<Genre> getAllGenres(){
        return this.genreRepository.findAll();
    }

    public List<Genre> getAllGenresByIds(List<Long> longs) {
        return this.genreRepository.findAllByGenreIdIn(longs);
    }
}
