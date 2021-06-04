package com.swoqe.newsstand.model.repositories;

import com.swoqe.newsstand.model.entities.RatePeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface PeriodRepository extends JpaRepository<RatePeriod, Long> {

}
