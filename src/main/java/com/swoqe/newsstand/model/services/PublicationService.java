package com.swoqe.newsstand.model.services;

import com.swoqe.newsstand.model.entities.Genre;
import com.swoqe.newsstand.model.entities.Publication;
import com.swoqe.newsstand.model.entities.Rate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PublicationService extends CrudPageableService<Publication, Long> {

    Page<Publication> findAllByTitle(String title, Pageable paging);

    Page<Publication> findAllByGenres(List<Genre> genres, Pageable paging);

    void savePublicationWithRates(Publication publication, List<Rate> rates);
}
