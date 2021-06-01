package com.swoqe.newsstand.model.services;

import com.swoqe.newsstand.model.entities.Genre;
import com.swoqe.newsstand.model.entities.Publication;
import com.swoqe.newsstand.model.repositories.PublicationRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PublicationService {

    private final PublicationRepository publicationRepository;

    public Page<Publication> getAllPublications(Pageable paging){
        return this.publicationRepository.findAllBy(paging);
    }

    public Page<Publication> getAllPublicationsByName(String title, Pageable paging) {
        return this.publicationRepository.findAllByTitle(title, paging);
    }

    public Page<Publication> getAllPublicationsByGenresAndName(List<Genre> genres, String title, Pageable paging) {
        return this.publicationRepository.findAllByGenresAndTitle(genres, title, paging);
    }

    public Page<Publication> getAllPublicationsByGenres(List<Genre> genres, Pageable paging) {
        return this.publicationRepository.findAllByGenres(genres, paging);
    }
}
