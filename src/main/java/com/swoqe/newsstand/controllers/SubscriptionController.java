package com.swoqe.newsstand.controllers;

import com.swoqe.newsstand.model.entities.Rate;
import com.swoqe.newsstand.model.entities.Subscription;
import com.swoqe.newsstand.model.entities.User;
import com.swoqe.newsstand.model.services.RateService;
import com.swoqe.newsstand.model.services.SubscriptionService;
import com.swoqe.newsstand.model.services.UserService;
import com.swoqe.newsstand.security.entity.MyUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
                                      RedirectAttributes redirectAttributes) {
        MyUserDetails userDetails = getUserDetails(authentication);
        User user = userService.findById(userDetails.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));
        Rate rate = rateService.findById(rateId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Unable to find publication rate!"));
        String message;
        boolean hasAlreadySubscribed = user.getSubscriptions().stream()
                .anyMatch((s) -> s.getRate().getPublication().equals(rate.getPublication()));
        if (hasAlreadySubscribed) {
            message = "You have already subscribed to this publication!";
        } else {
            message = this.userService.doSubscription(user, rate);
        }

        redirectAttributes.addFlashAttribute("information", message);
        return "redirect:/user/account";
    }

    @PostMapping("/cancel")
    public String cancelSubscriptionToUser(@RequestParam Long subscriptionId,
                                           Authentication authentication,
                                           RedirectAttributes redirectAttributes) {
        MyUserDetails userDetails = getUserDetails(authentication);
        User user = userService.findById(userDetails.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));

        Subscription subscription = this.subscriptionService.findById(subscriptionId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Unable to find subscription!"));

        String message = this.userService.cancelSubscription(user, subscription);
        redirectAttributes.addFlashAttribute("information", message);
        return "redirect:/user/account";
    }

    private MyUserDetails getUserDetails(Authentication authentication) {
        try {
            return (MyUserDetails) authentication.getPrincipal();
        } catch (ClassCastException ignored) {
            return new MyUserDetails((org.springframework.security.core.userdetails.User) authentication.getPrincipal());
        }
    }
}
