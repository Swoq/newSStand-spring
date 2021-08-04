package com.swoqe.newsstand.model.services;

import com.swoqe.newsstand.model.entities.Rate;

import java.util.List;

public interface RateService extends CrudService<Rate, Long> {

    void saveAll(List<Rate> rates);
}
