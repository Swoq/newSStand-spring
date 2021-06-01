package com.swoqe.newsstand.controllers;

import com.swoqe.newsstand.model.entities.Genre;
import com.swoqe.newsstand.model.entities.Publication;
import com.swoqe.newsstand.model.services.GenreService;
import com.swoqe.newsstand.model.services.PublicationService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/catalog")
@AllArgsConstructor
public class CatalogController {

    private final PublicationService publicationService;
    private final GenreService genreService;

    @GetMapping
    public String loadAllPublications(
            Model model,
            @RequestParam(required = false) Optional<String> title,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(required = false) Optional<List<Long>> optionalGenres,
            @RequestParam(defaultValue = "asc") String d,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){

        if (!sortBy.equalsIgnoreCase("title")
                && !sortBy.equalsIgnoreCase("price")
                && !sortBy.equalsIgnoreCase("publication_date"))
            sortBy = "title";
        Optional<Sort.Direction> optDirection = Sort.Direction.fromOptionalString(d);
        Sort.Direction direction = Sort.Direction.ASC;
        if (optDirection.isPresent())
            direction = optDirection.get();
        Pageable paging = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<Publication> publicationPage;

        if (optionalGenres.isEmpty())
            if(title.isEmpty())
                publicationPage = this.publicationService.getAllPublications(paging);
            else
                publicationPage = this.publicationService.getAllPublicationsByName(title.get(), paging);
        else {
            List<Genre> genres = genreService.getAllGenresByIds(optionalGenres.get());
            if (title.isEmpty())
                publicationPage = this.publicationService.getAllPublicationsByGenres(genres, paging);
            else
                publicationPage = this.publicationService.getAllPublicationsByName(title.get(), paging);
        }
        List<Publication> publications = publicationPage.getContent();
        model.addAttribute("currentPage", publicationPage.getNumber());
        model.addAttribute("totalItems", publicationPage.getTotalElements());
        model.addAttribute("totalPages", publicationPage.getTotalPages());
        model.addAttribute("publications", publications);
        List<Genre> allGenres = genreService.getAllGenres();
        model.addAttribute("genres", allGenres);
        return "catalog";
    }
}
