package com.swoqe.newsstand.model.repositories;

import com.swoqe.newsstand.model.entities.Subscription;
import com.swoqe.newsstand.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

}
