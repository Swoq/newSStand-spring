package com.swoqe.newsstand.security.registration;

import com.swoqe.newsstand.model.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;


@RequestMapping(path = "/registration")
@AllArgsConstructor
@Controller
public class RegistrationController {

    private final RegistrationService registrationService;

    @GetMapping
    public String getRegistrationPage(Model model){
        User user = new User();
        model.addAttribute("user", user);
        return "registration";
    }

    @PostMapping
    public String register(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return "registration";

        registrationService.register(user);
        return "redirect:/login";
    }

}
