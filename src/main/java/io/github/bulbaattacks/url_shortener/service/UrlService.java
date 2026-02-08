package io.github.bulbaattacks.url_shortener.service;

import io.github.bulbaattacks.url_shortener.entity.Url;
import io.github.bulbaattacks.url_shortener.dto.UrlDto;
import io.github.bulbaattacks.url_shortener.exception.NoUrlException;
import io.github.bulbaattacks.url_shortener.util.UrlHasher;
import io.github.bulbaattacks.url_shortener.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepository repository;

    public UrlDto createShortUrl(UrlDto dto) {
        var originalUrl = dto.url();
        var shortUrl = UrlHasher.hashUrl(originalUrl);
        var entity = Url.builder()
                .originalUrl(originalUrl)
                .shortUrl(shortUrl)
                .build();
        repository.save(entity);
        return new UrlDto(shortUrl);
    }

    public String getOriginalUrl(String shortUrl) {
        return repository
                .findByShortUrl(shortUrl)
                .map(Url::getOriginalUrl)
                .orElseThrow(() -> new NoUrlException(shortUrl));
    }
}
