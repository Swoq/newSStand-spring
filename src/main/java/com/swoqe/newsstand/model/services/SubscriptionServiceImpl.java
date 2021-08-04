package com.swoqe.newsstand.model.services;

import com.swoqe.newsstand.model.entities.Subscription;
import com.swoqe.newsstand.model.repositories.SubscriptionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    @Override
    public List<Subscription> findAll() {
        return null;
    }

    @Override
    public Optional<Subscription> findById(Long id) {
        return null;
    }

    @Override
    public Subscription save(Subscription object) {
        return null;
    }

    @Override
    public void delete(Subscription object) {

    }

    @Override
    public void deleteById(Long aLong) {

    }


}
