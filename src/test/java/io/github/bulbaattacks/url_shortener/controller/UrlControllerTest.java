package io.github.bulbaattacks.url_shortener.controller;

import io.github.bulbaattacks.url_shortener.dto.UrlDto;
import io.github.bulbaattacks.url_shortener.service.UrlService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UrlController.class)
class UrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UrlService service;

    @Test
    void shouldCreateShortUrl() throws Exception {
        UrlDto request = new UrlDto("https://example.com");
        UrlDto response = new UrlDto("abc123");

        Mockito.when(service.createShortUrl(any(UrlDto.class))).thenReturn(response);

        mockMvc.perform(post("/short")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "url": "https://example.com"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value("abc123"));
    }

    @Test
    void shouldRedirectToOriginalUrl() throws Exception {
        Mockito.when(service.getOriginalUrl(eq("abc123")))
                .thenReturn("https://example.com");

        mockMvc.perform(get("/abc123"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "https://example.com"));
    }

    @Test
    void shouldReturnExistedShortUrl() throws Exception {
        UrlDto request = new UrlDto("https://example.com");
        UrlDto response = new UrlDto("abc123");

        Mockito.when(service.createShortUrl(any(UrlDto.class)))
                .thenReturn(response);

        mockMvc.perform((post("/short")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""" 
                        {"url": "https://example.com"} 
                        """)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url")
                        .value("abc123"));
    }
}

