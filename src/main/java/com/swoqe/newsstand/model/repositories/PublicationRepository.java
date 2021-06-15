package com.swoqe.newsstand.model.repositories;

import com.swoqe.newsstand.model.entities.Genre;
import com.swoqe.newsstand.model.entities.Publication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface PublicationRepository extends CrudRepository<Publication, Long> {
    Page<Publication> findAllBy(Pageable pageable);
    Page<Publication> findAllByTitleContainsIgnoreCase(String title, Pageable pageable);
    Page<Publication> findAllByGenresIn(List<Genre> genres, Pageable pageable);

    Optional<Publication> findFirstByPublicationId(Long id);
}
