package com.swoqe.newsstand.model.services;

import com.swoqe.newsstand.model.entities.Genre;
import com.swoqe.newsstand.model.repositories.GenreRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Override
    public List<Genre> findAll() {
        return this.genreRepository.findAll();
    }

    @Override
    public Optional<Genre> findById(Long aLong) {
        return null;
    }

    @Override
    public List<Genre> findAllByIds(List<Long> longs) {
        return this.genreRepository.findAllByGenreIdIn(longs);
    }

    @Override
    public Genre save(Genre genre) {
        return this.genreRepository.save(genre);
    }

    @Override
    public void delete(Genre object) {

    }

    @Override
    public void deleteById(Long aLong) {

    }
}
