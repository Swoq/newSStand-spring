package com.swoqe.newsstand.controllers;

import com.swoqe.newsstand.model.entities.Publication;
import com.swoqe.newsstand.model.services.PublicationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PublicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PublicationService publicationService;

    @Test
    @DisplayName("GET /publication/{id}")
    void getPublicationPage() throws Exception {
        Publication publication = new Publication(
                1L, "Title1", "Desc",
                LocalDate.now(), "Publ", "/path",
                List.of(), List.of()
        );
        given(publicationService.getPublicationById(any())).willReturn(Optional.of(publication));
        MediaType textHtml = new MediaType(MediaType.TEXT_HTML, StandardCharsets.UTF_8);
        mockMvc.perform(get("/publication/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf(String.valueOf(textHtml))))
                .andExpect(model().attribute("publication", publication));
    }

    @Test
    @DisplayName("GET /publication/{id} NOT FOUND")
    void getPublicationPageNotFound() throws Exception {
        given(publicationService.getPublicationById(any())).willReturn(Optional.empty());
        MediaType textHtml = new MediaType(MediaType.TEXT_HTML, StandardCharsets.UTF_8);
        mockMvc.perform(get("/publication/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(model().attribute("statusCode", 404))
                .andExpect(content().contentType(MediaType.valueOf(String.valueOf(textHtml))));
    }

}