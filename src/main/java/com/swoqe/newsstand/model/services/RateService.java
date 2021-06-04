package com.swoqe.newsstand.model.services;

import com.swoqe.newsstand.model.entities.Rate;
import com.swoqe.newsstand.model.repositories.RateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class RateService {
    private final RateRepository rateRepository;

    public Optional<Rate> getRateById(Long id){
        return this.rateRepository.findById(id);
    }
}
