package com.swoqe.newsstand.model.services;

import com.swoqe.newsstand.model.entities.Rate;
import com.swoqe.newsstand.model.entities.RatePeriod;
import com.swoqe.newsstand.model.repositories.PeriodRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RatePeriodService {

    private final PeriodRepository periodRepository;

    public Optional<RatePeriod> getOneById(Long id){
        return this.periodRepository.findById(id);
    }

    public void addNewRatePeriod(RatePeriod ratePeriod){
        this.periodRepository.save(ratePeriod);
    }

    public List<RatePeriod> getAllRatePeriods(){
        return this.periodRepository.findAll();
    }

}
