package com.swoqe.newsstand.controllers;

import com.swoqe.newsstand.model.entities.Genre;
import com.swoqe.newsstand.model.entities.Publication;
import com.swoqe.newsstand.model.services.GenreService;
import com.swoqe.newsstand.model.services.PublicationService;
import com.swoqe.newsstand.util.OrderBy;
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
import java.util.Locale;
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
            @RequestParam(name = "title", required = false) Optional<String> paramOptionalTitle,
            @RequestParam(name = "sortBy", defaultValue = "title") String paramSortBy,
            @RequestParam(name = "genres", required = false) Optional<List<Long>> paramOptionalGenres,
            @RequestParam(name = "d", defaultValue = "asc") String paramD,
            @RequestParam(name = "page", defaultValue = "1") int paramPage,
            @RequestParam(name = "size", defaultValue = "5") int paramSize
    ){
        OrderBy orderBy = OrderBy.safeValueOf(paramSortBy);
        Sort.Direction direction = Sort.Direction.fromOptionalString(paramD).orElse(Sort.Direction.ASC);
        Sort.Order order = new Sort.Order(direction, orderBy.name()).ignoreCase();
        Pageable paging = PageRequest.of(paramPage-1, paramSize, Sort.by(order));

        Page<Publication> publicationPage;
        if (paramOptionalGenres.isEmpty()) {
            if (paramOptionalTitle.isEmpty())
                publicationPage = this.publicationService.getAllPublications(paging);
            else
                publicationPage = this.publicationService.getAllPublicationsByName(paramOptionalTitle.get(), paging);
        }
        else {
            List<Genre> genres = genreService.getAllGenresByIds(paramOptionalGenres.get());
            publicationPage = this.publicationService.getAllPublicationsByGenres(genres, paging);
        }

        List<Publication> publications = publicationPage.getContent();
        model.addAttribute("currentPage", publicationPage.getNumber()+1);
        model.addAttribute("totalItems", publicationPage.getTotalElements());
        model.addAttribute("totalPages", publicationPage.getTotalPages());
        model.addAttribute("publications", publications);
        List<Genre> allGenres = genreService.getAllGenres();
        model.addAttribute("genres", allGenres);
        return "catalog";
    }
}
