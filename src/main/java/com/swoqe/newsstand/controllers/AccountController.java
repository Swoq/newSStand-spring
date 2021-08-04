package com.swoqe.newsstand.controllers;

import com.swoqe.newsstand.model.entities.Subscription;
import com.swoqe.newsstand.model.entities.User;
import com.swoqe.newsstand.model.services.UserService;
import com.swoqe.newsstand.security.entity.MyUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user/account")
@AllArgsConstructor
public class AccountController {

    private final UserService userService;

    @GetMapping
    public String getAccountPage(HttpServletRequest request, Model model, Authentication authentication) {
        MyUserDetails userDetails;
        try {
            userDetails = (MyUserDetails) authentication.getPrincipal();
        } catch (ClassCastException ignored) {
            userDetails = new MyUserDetails((org.springframework.security.core.userdetails.User) authentication.getPrincipal());
        }
        User user = userService.findById(userDetails.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));

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
