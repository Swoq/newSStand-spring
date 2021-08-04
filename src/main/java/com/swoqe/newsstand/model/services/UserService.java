package com.swoqe.newsstand.model.services;

import com.swoqe.newsstand.model.entities.Rate;
import com.swoqe.newsstand.model.entities.Subscription;
import com.swoqe.newsstand.model.entities.User;

public interface UserService extends CrudService<User, Long> {

    void signUpUser(User user);

    String doSubscription(User user, Rate rate);

    String cancelSubscription(User user, Subscription subscription);

    String blockUserByEmail(String email);

    String unblockUserByEmail(String email);
}
