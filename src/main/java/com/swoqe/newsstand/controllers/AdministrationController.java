package com.swoqe.newsstand.controllers;

import com.swoqe.newsstand.model.entities.Genre;
import com.swoqe.newsstand.model.entities.Publication;
import com.swoqe.newsstand.model.entities.Rate;
import com.swoqe.newsstand.model.entities.RatePeriod;
import com.swoqe.newsstand.model.services.*;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
@Log4j2
public class AdministrationController {

    private final GenreService genreService;
    private final RatePeriodService ratePeriodService;
    private final UserService userService;
    private final PublicationService publicationService;

    private final String VALID_NAME_REGEX = "^[a-zA-Zа-яА-Я0-9-`']+$";
    private final String VALID_EMAIL_REGEX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    @PostMapping("/genres/add")
    public String addGenre(
            @RequestParam String genreName,
            @RequestParam String genreDescription,
            RedirectAttributes redirectAttributes
    ){
        String validationError = this.nameValidation(genreName);
        if (genreDescription.length() > 1000)
            validationError = "Description is too long!";
        if(validationError != null) {
            redirectAttributes.addFlashAttribute("error", validationError);
            return "redirect:/catalog";
        }
        genreService.addNewGenre(new Genre(genreName, genreDescription));
        redirectAttributes.addFlashAttribute("info", "New genre for publications has been successfully added");
        return "redirect:/catalog";
    }

    @PostMapping("/periods/add")
    public String addPeriod(
            @RequestParam String periodName,
            @RequestParam String periodDescription,
            @RequestParam int days,
            RedirectAttributes redirectAttributes
    ){
        String validationError = this.nameValidation(periodName);
        if (periodDescription.length() > 1000)
            validationError = "Description is too long!";
        if (days <= 0 )
            validationError = "Days cannot be 0 or negative!";
        if(validationError != null) {
            redirectAttributes.addFlashAttribute("error", validationError);
            return "redirect:/catalog";
        }
        this.ratePeriodService.addNewRatePeriod(new RatePeriod(Period.ofDays(days), periodName, periodDescription));
        redirectAttributes.addFlashAttribute("info", "New period for rates has been successfully added");
        return "redirect:/catalog";
    }

    @GetMapping("/publications/new")
    public String getNewPublicationPage(Model model) {
        model.addAttribute("publication", new Publication());
        model.addAttribute("genres", this.genreService.getAllGenres());
        model.addAttribute("periods", this.ratePeriodService.getAllRatePeriods());
        return "new_publication";
    }

    @PostMapping({"/publications/new", "/publications/edit"})
    public String createNewPublication(
            @ModelAttribute("publication") @Valid Publication publication,
            BindingResult bindingResult,
            @RequestParam(name = "periods", required = false) List<RatePeriod> periods,
            @RequestParam(name = "prices", required = false) List<BigDecimal> prices,
            RedirectAttributes redirectAttributes,
            Model model
    ){
        if(bindingResult.hasErrors())
            return "new_publication";
        String error = ratesValidation(periods, prices);
        if(error != null) {
            model.addAttribute("error", error);
            model.addAttribute("genres", this.genreService.getAllGenres());
            model.addAttribute("periods", this.ratePeriodService.getAllRatePeriods());
            return "new_publication";
        }

        List<Rate> rates = Rate.createNewList(periods, prices, publication);
        this.publicationService.savePublicationWithRates(publication, rates);
        redirectAttributes.addFlashAttribute("info", "New Publication has been successfully updated!");
        return "redirect:/catalog";
    }

    @GetMapping("/publications/edit/{id}")
    public String getEditPublicationPage(@PathVariable Long id, Model model) {

        Publication publication = this.publicationService.getPublicationById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Unable to find publication!"));

        model.addAttribute("publication", publication);
        model.addAttribute("genres", this.genreService.getAllGenres());
        model.addAttribute("periods", this.ratePeriodService.getAllRatePeriods());
        return "edit_publication";
    }


    @PostMapping("/users/block")
    public String blockUser(
            @RequestParam(name = "email") String email,
            RedirectAttributes redirectAttributes
    ){
        if(email.matches(VALID_EMAIL_REGEX) ){
            String message = this.userService.blockUserByEmail(email);
            redirectAttributes.addFlashAttribute("info", message);
        }
        else{
            redirectAttributes.addFlashAttribute("error", "Invalid email format!");
        }

        return "redirect:/catalog";
    }

    @PostMapping("/users/unblock")
    public String unblockUser(
            @RequestParam(name = "email") String email,
            RedirectAttributes redirectAttributes
    ){
        if(email.matches(VALID_EMAIL_REGEX) ){
            String message = this.userService.unblockUserByEmail(email);
            redirectAttributes.addFlashAttribute("info", message);
        }
        else{
            redirectAttributes.addFlashAttribute("error", "Invalid email format!");
        }

        return "redirect:/catalog";
    }

    private String ratesValidation(List<RatePeriod> periods, List<BigDecimal> prices){
        if(periods == null || periods.isEmpty() || prices == null || prices.isEmpty())
            return "Publication must have at least one rate!";
        if(periods.size() != prices.size())
            return "Periods and prices must be the same size!";
        boolean containNegative = prices.stream().anyMatch((s) -> s.compareTo(BigDecimal.ZERO) < 0);
        if(containNegative)
            return "Price cannot be negative!";
        boolean invalidPeriods = periods.stream().anyMatch((s) -> !validatePeriod(s));
        if(invalidPeriods)
            return "You have irrelevant information about rate periods. Please reload the page.";
        return null;
    }

    private boolean validatePeriod(RatePeriod ratePeriod){
        Optional<RatePeriod> optional = this.ratePeriodService.getOneById(ratePeriod.getPeriodId());
        if(optional.isEmpty())
            return false;
        else
            return optional.get().equals(ratePeriod);
    }

    private String nameValidation(String name){
        if(name == null || name.equals(""))
            return "Strings cannot be empty!";
        else if (name.length() < 1 || name.length() > 255)
            return "Name length should be between 1 and 255";
        else if(!name.matches(VALID_NAME_REGEX))
            return "Only letters are allowed!";
        return null;
    }
}
