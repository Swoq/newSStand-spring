package com.swoqe.newsstand.security.registration;

import com.swoqe.newsstand.model.entities.User;
import com.swoqe.newsstand.model.services.UserService;
import com.swoqe.newsstand.security.entity.UserRole;
import com.swoqe.newsstand.security.entity.MyUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final UserService userService;

    public void register(User user) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userService.signUpUser(user);
    }

}
