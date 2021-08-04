package com.swoqe.newsstand.model.services;

import com.swoqe.newsstand.model.entities.Genre;

import java.util.List;

public interface GenreService extends CrudService<Genre, Long>{
    List<Genre> findAllByIds(List<Long> longs);
}
