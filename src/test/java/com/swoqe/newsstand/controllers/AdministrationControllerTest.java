package com.swoqe.newsstand.controllers;

import com.swoqe.newsstand.model.entities.Genre;
import com.swoqe.newsstand.model.entities.Publication;
import com.swoqe.newsstand.model.entities.RatePeriod;
import com.swoqe.newsstand.model.services.GenreService;
import com.swoqe.newsstand.model.services.PublicationService;
import com.swoqe.newsstand.model.services.RatePeriodService;
import com.swoqe.newsstand.model.services.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AdministrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenreService genreService;

    @MockBean
    private RatePeriodService ratePeriodService;

    @MockBean
    private UserService userService;

    @MockBean
    private PublicationService publicationService;

    @Test
    @WithMockUser(value = "admin", authorities = "ADMIN")
    @DisplayName(value = "POST /admin/genres/add Done")
    void addGenre() throws Exception {
        String name = "name";
        String description = "description";

        mockMvc.perform(post("/admin/genres/add")
                .param("genreName", name)
                .param("genreDescription", description)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("info", "New genre for publications has been successfully added"));

        ArgumentCaptor<Genre> genreArgumentCaptor = ArgumentCaptor.forClass(Genre.class);
        verify(genreService).addNewGenre(genreArgumentCaptor.capture());

        Genre genre = genreArgumentCaptor.getValue();
        assertThat(name).isEqualTo(genre.getGenreName());
        assertThat(description).isEqualTo(genre.getDescription());
    }

    @Test
    @WithMockUser(value = "admin", authorities = "ADMIN")
    @DisplayName(value = "POST /admin/genres/add DescriptionLength>1000")
    void shouldNotAddGenreWithDescriptionOver1000() throws Exception {
        String name = "name";
        String description = "description".repeat(100);

        mockMvc.perform(post("/admin/genres/add")
                .param("genreName", name)
                .param("genreDescription", description)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("error", "Description is too long!"));
    }

    @Test
    @WithMockUser(value = "admin", authorities = "ADMIN")
    @DisplayName(value = "POST /admin/genres/add TitleOverFormat")
    void shouldNotAddGenreWithTitleOverFormat() throws Exception {
        String name = "####";
        String description = "description";

        mockMvc.perform(post("/admin/genres/add")
                .param("genreName", name)
                .param("genreDescription", description)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("error", "Only letters are allowed!"));
    }

    @Test
    @WithMockUser(value = "admin", authorities = "ADMIN")
    @DisplayName(value = "POST /admin/genres/add empty")
    void shouldNotAddGenreEmpty() throws Exception {
        String name = "";
        String description = "";

        mockMvc.perform(post("/admin/genres/add")
                .param("genreName", name)
                .param("genreDescription", description)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("error", "Strings cannot be empty!"));
    }

    @Test
    @WithMockUser(value = "admin", authorities = "ADMIN")
    @DisplayName(value = "POST /admin/genres/add Title>255")
    void shouldNotAddGenreWithTitleOver255() throws Exception {
        String name = "x".repeat(256);
        String description = "description";

        mockMvc.perform(post("/admin/genres/add")
                .param("genreName", name)
                .param("genreDescription", description)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("error", "Name length should be between 1 and 255"));
    }

    @Test
    @WithMockUser(value = "admin", authorities = "ADMIN")
    @DisplayName(value = "POST /admin/periods/add done")
    void addPeriod() throws Exception {
        String name = "name";
        String description = "description";
        String days = "10";

        mockMvc.perform(post("/admin/periods/add")
                .param("periodName", name)
                .param("periodDescription", description)
                .param("days", days)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("info", "New period for rates has been successfully added"));

        ArgumentCaptor<RatePeriod> ratePeriodArgumentCaptor = ArgumentCaptor.forClass(RatePeriod.class);
        verify(ratePeriodService).addNewRatePeriod(ratePeriodArgumentCaptor.capture());

        RatePeriod ratePeriod = ratePeriodArgumentCaptor.getValue();
        assertThat(name).isEqualTo(ratePeriod.getFormalName());
        assertThat(description).isEqualTo(ratePeriod.getDescription());
        assertThat(Integer.parseInt(days)).isEqualTo(ratePeriod.getPeriod().getDays());
    }

    @Test
    @WithMockUser(value = "admin", authorities = "ADMIN")
    @DisplayName(value = "POST /admin/periods/add DescriptionLength>1000")
    void shouldNotAddPeriodWithDescriptionOver1000() throws Exception {
        String name = "name";
        String description = "description".repeat(100);
        String days = "10";

        mockMvc.perform(post("/admin/periods/add")
                .param("periodName", name)
                .param("periodDescription", description)
                .param("days", days)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("error", "Description is too long!"));
    }

    @Test
    @WithMockUser(value = "admin", authorities = "ADMIN")
    @DisplayName(value = "POST /admin/periods/add TitleOverFormat")
    void shouldNotAddPeriodWithTitleOverFormat() throws Exception {
        String name = "####";
        String description = "description";
        String days = "10";

        mockMvc.perform(post("/admin/periods/add")
                .param("periodName", name)
                .param("periodDescription", description)
                .param("days", days)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("error", "Only letters are allowed!"));
    }

    @Test
    @WithMockUser(value = "admin", authorities = "ADMIN")
    @DisplayName(value = "POST /admin/periods/add empty")
    void shouldNotAddPeriodEmpty() throws Exception {
        String name = "";
        String description = "";
        String days = "10";

        mockMvc.perform(post("/admin/periods/add")
                .param("periodName", name)
                .param("periodDescription", description)
                .param("days", days)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("error", "Strings cannot be empty!"));
    }

    @Test
    @WithMockUser(value = "admin", authorities = "ADMIN")
    @DisplayName(value = "POST /admin/periods/add Title>255")
    void shouldNotAddPeriodWithTitleOver255() throws Exception {
        String name = "x".repeat(256);
        String description = "description";
        String days = "10";

        mockMvc.perform(post("/admin/periods/add")
                .param("periodName", name)
                .param("periodDescription", description)
                .param("days", days)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("error", "Name length should be between 1 and 255"));
    }

    @Test
    @WithMockUser(value = "admin", authorities = "ADMIN")
    @DisplayName(value = "POST /admin/periods/add DaysNegative")
    void shouldNotAddPeriodWithDaysNegative() throws Exception {
        String name = "x1";
        String description = "description";
        String days = "-1";

        mockMvc.perform(post("/admin/periods/add")
                .param("periodName", name)
                .param("periodDescription", description)
                .param("days", days)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("error", "Days cannot be 0 or negative!"));
    }

    @Test
    @WithMockUser(value = "admin", authorities = "ADMIN")
    @DisplayName(value = "GET /admin/publications/new")
    void getNewPublicationPage() throws Exception {
        List<Genre> genres = List.of(new Genre(1L, "genre1", "description1", List.of()),
                new Genre(2L, "genre1", "description1", List.of()),
                new Genre(3L, "genre1", "description1", List.of()),
                new Genre(4L, "genre1", "description1", List.of()));

        List<RatePeriod> periods = List.of(new RatePeriod(1L, Period.ZERO, "", ""),
                new RatePeriod(2L, Period.ZERO, "", ""),
                new RatePeriod(3L, Period.ZERO, "", ""),
                new RatePeriod(4L, Period.ZERO, "", ""));
        given(genreService.getAllGenres()).willReturn(genres);
        given(ratePeriodService.getAllRatePeriods()).willReturn(periods);
        MediaType textHtml = new MediaType(MediaType.TEXT_HTML, StandardCharsets.UTF_8);
        mockMvc.perform(get("/admin/publications/new"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf(String.valueOf(textHtml))))
                .andExpect(model().attribute("genres", genres))
                .andExpect(model().attribute("periods", periods));
    }


    @Test
    @WithMockUser(value = "admin", authorities = "ADMIN")
    @DisplayName(value = "GET /admin/publications/edit/{id}")
    void getEditPublicationPage() throws Exception {
        List<Genre> genres = List.of(new Genre(1L, "genre1", "description1", List.of()),
                new Genre(2L, "genre1", "description1", List.of()),
                new Genre(3L, "genre1", "description1", List.of()),
                new Genre(4L, "genre1", "description1", List.of()));

        List<RatePeriod> periods = List.of(new RatePeriod(1L, Period.ZERO, "", ""),
                new RatePeriod(2L, Period.ZERO, "", ""),
                new RatePeriod(3L, Period.ZERO, "", ""),
                new RatePeriod(4L, Period.ZERO, "", ""));
        Long id = 1L;
        Publication publication = new Publication(1L, "Title4", "Desc", LocalDate.now(), "Publ", "/path", List.of(), List.of());
        given(publicationService.getPublicationById(id)).willReturn(Optional.of(publication));
        given(genreService.getAllGenres()).willReturn(genres);
        given(ratePeriodService.getAllRatePeriods()).willReturn(periods);

        MediaType textHtml = new MediaType(MediaType.TEXT_HTML, StandardCharsets.UTF_8);
        mockMvc.perform(get("/admin/publications/edit/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf(String.valueOf(textHtml))))
                .andExpect(model().attribute("publication", publication))
                .andExpect(model().attribute("genres", genres))
                .andExpect(model().attribute("periods", periods));
    }

    @Test
    @WithMockUser(value = "admin", authorities = "ADMIN")
    @DisplayName(value = "GET /admin/publications/edit/{id}")
    void getEditPublicationPageNotFound() throws Exception {
        Long id = 1L;
        given(publicationService.getPublicationById(id)).willReturn(Optional.empty());

        MediaType textHtml = new MediaType(MediaType.TEXT_HTML, StandardCharsets.UTF_8);
        mockMvc.perform(get("/admin/publications/edit/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf(String.valueOf(textHtml))))
                .andExpect(model().attribute("statusCode", 404));
    }

    @Test
    @WithMockUser(value = "admin", authorities = "ADMIN")
    @DisplayName(value = "POST /admin/users/block")
    void blockUser() throws Exception {
        String email = "email@gmail.com";
        String message = "User has been successfully blocked!";
        given(userService.blockUserByEmail(email)).willReturn(message);
        mockMvc.perform(post("/admin/users/block").param("email", email))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("info", message));
    }

    @Test
    @WithMockUser(value = "admin", authorities = "ADMIN")
    @DisplayName(value = "POST /admin/users/block InvalidFormat")
    void shouldNotBlockEmailFormat() throws Exception {
        String email = "invalid";
        String message = "Invalid email format!";
        mockMvc.perform(post("/admin/users/block").param("email", email))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("error", message));
    }

    @Test
    @WithMockUser(value = "admin", authorities = "ADMIN")
    @DisplayName(value = "POST /admin/users/unblock")
    void unBlockUser() throws Exception {
        String email = "email@gmail.com";
        String message = "User has been successfully unblocked!";
        given(userService.unblockUserByEmail(email)).willReturn(message);
        mockMvc.perform(post("/admin/users/unblock").param("email", email))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("info", message));
    }

    @Test
    @WithMockUser(value = "admin", authorities = "ADMIN")
    @DisplayName(value = "POST /admin/users/unblock InvalidFormat")
    void shouldNotUnBlockEmailFormat() throws Exception {
        String email = "invalid";
        String message = "Invalid email format!";
        mockMvc.perform(post("/admin/users/unblock").param("email", email))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("error", message));
    }

}