package com.swoqe.newsstand.model.services;

import com.swoqe.newsstand.model.entities.Genre;
import com.swoqe.newsstand.model.entities.Publication;
import com.swoqe.newsstand.model.entities.Rate;
import com.swoqe.newsstand.model.repositories.PublicationRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PublicationService {

    private final PublicationRepository publicationRepository;
    private final RateService rateService;

    public Page<Publication> getAllPublications(Pageable paging){
        return this.publicationRepository.findAllBy(paging);
    }

    public Page<Publication> getAllPublicationsByName(String title, Pageable paging) {
        return this.publicationRepository.findAllByTitleContainsIgnoreCase(title, paging);
    }

    public Page<Publication> getAllPublicationsByGenres(List<Genre> genres, Pageable paging) {
        return this.publicationRepository.findAllByGenresIn(genres, paging);
    }

    public Optional<Publication> getPublicationById(Long id) {
        return this.publicationRepository.findFirstByPublicationId(id);
    }

    @Transactional
    public void savePublicationWithRates(Publication publication, List<Rate> rates) {
        this.publicationRepository.save(publication);
        this.rateService.saveAllRates(rates);
    }
}
