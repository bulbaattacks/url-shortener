package io.github.bulbaattacks.url_shortener.service;

import io.github.bulbaattacks.url_shortener.dto.UrlDto;
import io.github.bulbaattacks.url_shortener.entity.Url;
import io.github.bulbaattacks.url_shortener.exception.AlreadyExistsUrlException;
import io.github.bulbaattacks.url_shortener.exception.NoUrlException;
import io.github.bulbaattacks.url_shortener.repository.UrlRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UrlServiceTest {

    @Mock
    private UrlRepository repository;

    @InjectMocks
    private UrlService service;

    @Test
    void createShortUrl_shouldThrowAlreadyExistsUrlException_whenOriginalUrlExists() {
        String url = "https://example.com";
        UrlDto dto = new UrlDto(url);

        Mockito.when(repository.findByOriginalUrl(url))
                .thenReturn(Optional.of(new Url()));

        assertThrows(AlreadyExistsUrlException.class, () -> service.createShortUrl(dto));
    }

    @Test
    void getOriginalUrl_shouldThrowNoUrlException_whenShortUrlNotFound() {
        String shortUrl = "abc123";

        Mockito.when(repository.findByShortUrl(shortUrl))
                .thenReturn(Optional.empty());

        assertThrows(NoUrlException.class, () -> service.getOriginalUrl(shortUrl));
    }

    @Test
    void createShortUrl_shouldSaveAndReturnShortUrl_whenOriginalUrlNotExists() {
        String url = "https://example.com";
        UrlDto dto = new UrlDto(url);

        Mockito.when(repository.findByOriginalUrl(url))
                .thenReturn(Optional.empty());
        Mockito.when(repository.save(Mockito.any()))
                .thenAnswer(invocation -> invocation.getArgument(0));
        UrlDto result = service.createShortUrl(dto);

        assertNotNull(result);
        assertNotNull(result.url());
        assertNotEquals(url, result.url());

        Mockito.verify(repository).findByOriginalUrl(url);
        Mockito.verify(repository).save(Mockito.any(Url.class));
    }

    @Test void getOriginalUrl_shouldReturnOriginalUrl_whenShortUrlExists() {
        String shortUrl = "abc123";
        String originalUrl = "https://example.com";
        Url entity = Url.builder()
                .shortUrl(shortUrl)
                .originalUrl(originalUrl)
                .build();

        Mockito.when(repository.findByShortUrl(shortUrl))
                .thenReturn(Optional.of(entity));
        String result = service.getOriginalUrl(shortUrl);

        assertEquals(originalUrl, result);

        Mockito.verify(repository).findByShortUrl(shortUrl); }
    }

