package com.swoqe.newsstand.controllers;

import com.swoqe.newsstand.model.entities.Publication;
import com.swoqe.newsstand.model.services.PublicationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Controller
@AllArgsConstructor
public class PublicationController {

    private final PublicationService publicationService;

    @RequestMapping("publication/{id}")
    public String getPublicationPage(@PathVariable Long id, Model model) {
        Publication publication = this.publicationService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Unable to find publication!"));
        model.addAttribute("publication", publication);
        return "publication";
    }
}
