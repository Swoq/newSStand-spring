package com.swoqe.newsstand.controllers;

import com.swoqe.newsstand.model.entities.Subscription;
import com.swoqe.newsstand.model.entities.User;
import com.swoqe.newsstand.model.services.SubscriptionService;
import com.swoqe.newsstand.model.services.UserService;
import com.swoqe.newsstand.security.entity.MyUserDetails;
import com.swoqe.newsstand.util.AnswerType;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user/account")
@AllArgsConstructor
public class AccountController {

    private final UserService userService;

    @GetMapping
    public String getAccountPage(HttpServletRequest request, Model model, Authentication authentication){
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        User user = userService.getUserById(userDetails.getId())
                .orElseThrow(() -> new AccessDeniedException("Access Denied!"));

        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if (inputFlashMap != null) {
            String information = (String) inputFlashMap.get("information");
            model.addAttribute("information", information);
        }
        List<Subscription> subscriptions = user.getSubscriptions();
        model.addAttribute("user", user);
        model.addAttribute("subscriptions", subscriptions);
        return "account";
    }
}
