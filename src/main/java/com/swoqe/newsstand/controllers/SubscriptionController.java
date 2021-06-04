package com.swoqe.newsstand.controllers;

import com.swoqe.newsstand.model.entities.Rate;
import com.swoqe.newsstand.model.entities.Subscription;
import com.swoqe.newsstand.model.entities.User;
import com.swoqe.newsstand.model.services.RateService;
import com.swoqe.newsstand.model.services.SubscriptionService;
import com.swoqe.newsstand.model.services.UserService;
import com.swoqe.newsstand.security.entity.MyUserDetails;
import com.swoqe.newsstand.util.AnswerType;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Controller
@RequestMapping("/user/subscriptions")
@AllArgsConstructor
public class SubscriptionController {

    private final UserService userService;
    private final RateService rateService;
    private final SubscriptionService subscriptionService;

    @PostMapping("/add")
    public String subscribeRateToUser(@RequestParam Long rateId,
                                      Authentication authentication,
                                      RedirectAttributes redirectAttributes){
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        User user = userService.getUserById(userDetails.getId())
                .orElseThrow(() -> new AccessDeniedException("Access Denied!"));
        Rate rate = rateService.getRateById(rateId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Unable to find publication rate!"));
        String message;
        boolean hasAlreadySubscribed = user.getSubscriptions().stream()
                .anyMatch((s) -> s.getRate().getPublication().equals(rate.getPublication()));
        if(hasAlreadySubscribed) {
            message = "You have already subscribed to this publication!";
        }
        else{
            message = this.userService.doSubscription(user, rate);
        }

        redirectAttributes.addFlashAttribute("information", message);
        return "redirect:/user/account";
    }

    @PostMapping("/cancel")
    public String cancelSubscriptionToUser(@RequestParam Long subscriptionId,
                                      Authentication authentication,
                                      RedirectAttributes redirectAttributes){
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        User user = userService.getUserById(userDetails.getId())
                .orElseThrow(() -> new AccessDeniedException("Access Denied!"));

        Subscription subscription = this.subscriptionService.getSubscriptionById(subscriptionId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Unable to find subscription!"));

        String message = this.userService.cancelSubscription(user, subscription);
        redirectAttributes.addFlashAttribute("information", message);
        return "redirect:/user/account";
    }
}
