package com.swoqe.newsstand.model.services;

import com.swoqe.newsstand.model.entities.RatePeriod;
import com.swoqe.newsstand.model.repositories.PeriodRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RatePeriodServiceImpl implements RatePeriodService {

    private final PeriodRepository periodRepository;

    @Override
    public Optional<RatePeriod> findById(Long id) {
        return null;
    }

    @Override
    public RatePeriod save(RatePeriod ratePeriod) {
        return this.periodRepository.save(ratePeriod);
    }

    @Override
    public void delete(RatePeriod object) {

    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public List<RatePeriod> findAll() {
        return this.periodRepository.findAll();
    }

}
