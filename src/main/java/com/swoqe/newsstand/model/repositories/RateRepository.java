package com.swoqe.newsstand.model.repositories;

import com.swoqe.newsstand.model.entities.Rate;
import com.swoqe.newsstand.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface RateRepository extends JpaRepository<Rate, Long> {

}
