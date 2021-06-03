package com.swoqe.newsstand.model.repositories;

import com.swoqe.newsstand.model.entities.Genre;
import com.swoqe.newsstand.model.entities.Publication;
import com.swoqe.newsstand.model.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
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


}
