package com.swoqe.newsstand.model.services;

import com.swoqe.newsstand.model.entities.Subscription;
import com.swoqe.newsstand.model.entities.User;
import com.swoqe.newsstand.model.repositories.SubscriptionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    public Optional<Subscription> getSubscriptionById(Long id){
        return this.subscriptionRepository.findById(id);
    }


}
