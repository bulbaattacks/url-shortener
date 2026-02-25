package io.github.bulbaattacks.url_shortener.service;

import io.github.bulbaattacks.url_shortener.dto.UrlDto;
import io.github.bulbaattacks.url_shortener.entity.Url;
import io.github.bulbaattacks.url_shortener.exception.NoUrlException;
import io.github.bulbaattacks.url_shortener.exception.UrlSaveException;
import io.github.bulbaattacks.url_shortener.repository.UrlRepository;
import io.github.bulbaattacks.url_shortener.util.UrlHasher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class UrlServiceTest {

    @Mock
    private UrlRepository repository;

    @InjectMocks
    private UrlService service;

    @Test
    void createShortUrl_shouldThrowUrlSaveException_whenOriginalUrlExists() {
        String originalUrl = "https://example.com";
        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).save(any(Url.class));
        assertThrows(UrlSaveException.class, () -> service.createShortUrl(new UrlDto(originalUrl)));
        Mockito.verify(repository).save(any(Url.class));
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
        String originalUrl = "https://example.com";
        String newShortUrl = "xyz789";

        try (MockedStatic<UrlHasher> mocked = Mockito.mockStatic(UrlHasher.class)) {
            mocked.when(() -> UrlHasher.hashUrl(originalUrl))
                    .thenReturn(newShortUrl);
            UrlDto result = service.createShortUrl(new UrlDto(originalUrl));

            assertEquals(newShortUrl, result.url());
            Mockito.verify(repository).save(any(Url.class));
        }
    }


    @Test
    void getOriginalUrl_shouldReturnOriginalUrl_whenShortUrlExists() {
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

        Mockito.verify(repository).findByShortUrl(shortUrl);
    }
}

