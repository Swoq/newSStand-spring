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
public class PublicationServiceImpl implements PublicationService {

    private final PublicationRepository publicationRepository;
    private final RateServiceImpl rateService;

    @Override
    public Page<Publication> findAll(Pageable paging) {
        return this.publicationRepository.findAllBy(paging);
    }

    @Override
    public Page<Publication> findAllByTitle(String title, Pageable paging) {
        return this.publicationRepository.findAllByTitleContainsIgnoreCase(title, paging);
    }

    @Override
    public Page<Publication> findAllByGenres(List<Genre> genres, Pageable paging) {
        return this.publicationRepository.findAllByGenresIn(genres, paging);
    }

    @Override
    @Transactional
    public void savePublicationWithRates(Publication publication, List<Rate> rates) {
        this.publicationRepository.save(publication);
        this.rateService.saveAll(rates);
    }

    @Override
    public List<Publication> findAll() {
        return null;
    }

    @Override
    public Optional<Publication> findById(Long aLong) {
        return null;
    }

    @Override
    public Publication save(Publication object) {
        return null;
    }

    @Override
    public void delete(Publication object) {

    }

    @Override
    public void deleteById(Long aLong) {

    }
}
