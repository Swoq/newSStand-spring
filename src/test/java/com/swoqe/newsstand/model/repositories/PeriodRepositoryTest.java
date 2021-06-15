package com.swoqe.newsstand.model.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class PeriodRepositoryTest {

    @Autowired
    private PeriodRepository repository;

    @Test
    @Transactional
    void init(){}
}