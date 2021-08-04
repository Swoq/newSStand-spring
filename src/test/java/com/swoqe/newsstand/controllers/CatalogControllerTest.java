package com.swoqe.newsstand.controllers;

import com.swoqe.newsstand.model.entities.Genre;
import com.swoqe.newsstand.model.entities.Publication;
import com.swoqe.newsstand.model.services.GenreService;
import com.swoqe.newsstand.model.services.PublicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CatalogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PublicationService publicationService;

    @MockBean
    private GenreService genreService;

    private List<Publication> publications;
    private List<Genre> genres;


    @BeforeEach
    private void init() {
        publications = List.of(
                new Publication(1L, "Title1", "Desc", LocalDate.now(), "Publ", "/path", List.of(), List.of()),
                new Publication(2L, "Title2", "Desc", LocalDate.now(), "Publ", "/path", List.of(), List.of()),
                new Publication(3L, "Title3", "Desc", LocalDate.now(), "Publ", "/path", List.of(), List.of()),
                new Publication(4L, "Title4", "Desc", LocalDate.now(), "Publ", "/path", List.of(), List.of())
        );
        genres = List.of(new Genre(1L, "genre1", "description1", List.of()),
                new Genre(2L, "genre1", "description1", List.of()),
                new Genre(3L, "genre1", "description1", List.of()),
                new Genre(4L, "genre1", "description1", List.of()));
    }

    @Test
    @DisplayName("GET /catalog")
    void loadAllPublicationsByDefault() throws Exception {
        Page<Publication> page = new PageImpl<>(publications);
        given(publicationService.findAll(any())).willReturn(page);
        given(genreService.findAll()).willReturn(genres);

        MediaType textHtml = new MediaType(MediaType.TEXT_HTML, StandardCharsets.UTF_8);
        mockMvc.perform(get("/catalog").flashAttr("info", ""))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf(String.valueOf(textHtml))))
                .andExpect(model().attribute("publications", publications))
                .andExpect(model().attribute("genres", genres));

    }

    @Test
    @DisplayName("GET /catalog?page=1&size=5")
    void loadAllPublicationsWithPageAndSize() throws Exception {
        Page<Publication> page = new PageImpl<>(publications);
        given(publicationService.findAll(any())).willReturn(page);
        given(genreService.findAll()).willReturn(genres);

        MediaType textHtml = new MediaType(MediaType.TEXT_HTML, StandardCharsets.UTF_8);
        mockMvc.perform(get("/catalog")
                .param("page", "1")
                .param("size", "5")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf(String.valueOf(textHtml))))
                .andExpect(model().attribute("publications", publications))
                .andExpect(model().attribute("genres", genres));

    }

    @Test
    @DisplayName("GET /catalog?page=1&size=5&d=asc&sortBy=title")
    void loadAllPublicationsWithPageAndSizeAndDirectionAndSortBy() throws Exception {
        Page<Publication> page = new PageImpl<>(publications);
        given(publicationService.findAll(any())).willReturn(page);
        given(genreService.findAll()).willReturn(genres);

        MediaType textHtml = new MediaType(MediaType.TEXT_HTML, StandardCharsets.UTF_8);
        mockMvc.perform(
                get("/catalog")
                        .param("page", "1")
                        .param("size", "5")
                        .param("d", "asc")
                        .param("sortBy", "title")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf(String.valueOf(textHtml))))
                .andExpect(model().attribute("publications", publications))
                .andExpect(model().attribute("genres", genres));

    }

    @Test
    @DisplayName("GET /catalog?page=1&size=5&d=asc&sortBy=title&genres=[]")
    void loadAllPublicationsWithPageAndSizeAndDirectionAndSortByAndGenres() throws Exception {
        Page<Publication> page = new PageImpl<>(publications);
        List<Long> ids = genres.stream()
                .map(Genre::getGenreId).collect(Collectors.toList());
        given(publicationService.findAllByGenres(any(), any())).willReturn(page);
        given(genreService.findAllByIds(ids)).willReturn(genres);
        given(genreService.findAll()).willReturn(genres);

        MediaType textHtml = new MediaType(MediaType.TEXT_HTML, StandardCharsets.UTF_8);
        mockMvc.perform(
                get("/catalog")
                        .param("page", "1")
                        .param("size", "5")
                        .param("d", "asc")
                        .param("sortBy", "title").param("genres", "")
                        .param("genres", "1")
                        .param("genres", "2")
                        .param("genres", "3")
                        .param("genres", "4")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf(String.valueOf(textHtml))))
                .andExpect(model().attribute("publications", publications))
                .andExpect(model().attribute("genres", genres));

    }

    @Test
    @DisplayName("GET /catalog?page=1&size=5&d=asc&sortBy=title&genres=[]&title=test")
    void loadAllPublicationsWithPageAndSizeAndDirectionAndSortByAndGenresAndTitle() throws Exception {
        Page<Publication> page = new PageImpl<>(publications);
        List<Long> ids = genres.stream()
                .map(Genre::getGenreId).collect(Collectors.toList());
        given(publicationService.findAllByGenres(any(), any())).willReturn(page);
        given(genreService.findAllByIds(ids)).willReturn(genres);
        given(genreService.findAll()).willReturn(genres);

        MediaType textHtml = new MediaType(MediaType.TEXT_HTML, StandardCharsets.UTF_8);
        mockMvc.perform(
                get("/catalog")
                        .param("page", "1")
                        .param("size", "5")
                        .param("d", "asc")
                        .param("sortBy", "title").param("genres", "")
                        .param("genres", "1")
                        .param("genres", "2")
                        .param("genres", "3")
                        .param("genres", "4")
                        .param("title", "test")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf(String.valueOf(textHtml))))
                .andExpect(model().attribute("publications", publications))
                .andExpect(model().attribute("genres", genres));

    }

    @Test
    @DisplayName("GET /catalog?page=1&size=5&d=asc&sortBy=title&title=test")
    void loadAllPublicationsWithPageAndSizeAndDirectionAndSortByAndTitle() throws Exception {
        Page<Publication> page = new PageImpl<>(publications);
        given(publicationService.findAllByTitle(eq("test"), any())).willReturn(page);
        given(genreService.findAll()).willReturn(genres);

        MediaType textHtml = new MediaType(MediaType.TEXT_HTML, StandardCharsets.UTF_8);
        mockMvc.perform(
                get("/catalog")
                        .param("page", "1")
                        .param("size", "5")
                        .param("d", "asc")
                        .param("sortBy", "title").param("genres", "")
                        .param("title", "test")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf(String.valueOf(textHtml))))
                .andExpect(model().attribute("publications", publications))
                .andExpect(model().attribute("genres", genres));

    }
}