package com.swoqe.newsstand.model.repositories;

import com.swoqe.newsstand.model.entities.Genre;
import com.swoqe.newsstand.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface GenreRepository extends JpaRepository<Genre, Long> {
    List<Genre> findAllByGenreIdIn(List<Long> ids);
}
