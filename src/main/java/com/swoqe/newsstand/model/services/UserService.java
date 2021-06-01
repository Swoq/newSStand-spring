package com.swoqe.newsstand.model.services;

import com.swoqe.newsstand.model.entities.User;
import com.swoqe.newsstand.model.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void signUpUser(User user){
        System.out.println(user);
        this.userRepository.save(user);
    }
}
