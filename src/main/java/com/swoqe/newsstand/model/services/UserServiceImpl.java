package com.swoqe.newsstand.model.services;

import com.swoqe.newsstand.model.entities.Rate;
import com.swoqe.newsstand.model.entities.Subscription;
import com.swoqe.newsstand.model.entities.User;
import com.swoqe.newsstand.model.repositories.SubscriptionRepository;
import com.swoqe.newsstand.model.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Override
    public void signUpUser(User user) {
        this.userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public Optional<User> findById(Long id) {
        return null;
    }

    @Override
    public User save(User object) {
        return null;
    }

    @Override
    public void delete(User object) {

    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    @Transactional
    public String doSubscription(User user, Rate rate) {
        BigDecimal account = user.getAccount();
        BigDecimal price = rate.getPrice();
        if (account.compareTo(price) < 0) {
            return "You don't have enough money to subscribe";
        }
        user.accountMinus(price);
        this.userRepository.save(user);
        LocalDate start = LocalDate.now();
        LocalDate end = start.plus(rate.getRatePeriod().getPeriod());
        Subscription subscription = new Subscription(user, rate, start, end);
        subscriptionRepository.save(subscription);
        return "You have successfully subscribed";
    }

    @Override
    @Transactional
    public String cancelSubscription(User user, Subscription subscription) {
        boolean userOwnSubscription = subscription.getUser().equals(user);
        if (!userOwnSubscription)
            throw new ResponseStatusException(NOT_FOUND, "Unable to bind user with that subscription!");
        subscription.setEndDate(LocalDate.now());

        this.subscriptionRepository.save(subscription);
        return "You have successfully unsubscribed!";
    }

    @Override
    @Transactional
    public String blockUserByEmail(String email) {
        Optional<User> optional = this.userRepository.findByEmail(email);
        String message;
        if (optional.isPresent()) {
            User user = optional.get();
            user.setLocked(true);
            userRepository.save(user);
            message = "User has been successfully blocked!";
        } else
            message = "Unable to find user with email: " + email;
        return message;
    }

    @Override
    @Transactional
    public String unblockUserByEmail(String email) {
        Optional<User> optional = this.userRepository.findByEmail(email);
        String message;
        if (optional.isPresent()) {
            User user = optional.get();
            user.setLocked(false);
            userRepository.save(user);
            message = "User has been successfully unblocked!";
        } else
            message = "Unable to find user with email: " + email;
        return message;
    }
}
