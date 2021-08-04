package com.swoqe.newsstand.model.services;

import com.swoqe.newsstand.model.entities.Rate;
import com.swoqe.newsstand.model.repositories.RateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RateServiceImpl implements RateService {
    private final RateRepository rateRepository;

    @Override
    public List<Rate> findAll() {
        return null;
    }

    @Override
    public Optional<Rate> findById(Long id) {
        return null;
    }

    @Override
    public Rate save(Rate object) {
        return null;
    }

    @Override
    public void delete(Rate object) {

    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void saveAll(List<Rate> rates) {
        this.rateRepository.saveAll(rates);
    }
}
