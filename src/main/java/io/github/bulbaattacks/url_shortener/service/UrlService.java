package io.github.bulbaattacks.url_shortener.service;

import io.github.bulbaattacks.url_shortener.dto.UrlDto;
import io.github.bulbaattacks.url_shortener.entity.Url;
import io.github.bulbaattacks.url_shortener.exception.AlreadyExistsUrlException;
import io.github.bulbaattacks.url_shortener.exception.NoUrlException;
import io.github.bulbaattacks.url_shortener.repository.UrlRepository;
import io.github.bulbaattacks.url_shortener.util.UrlHasher;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepository repository;

    public UrlDto createShortUrl(UrlDto dto) {
        var originalUrl = dto.url();
        var newShortUrl = UrlHasher.hashUrl(originalUrl);
        var entity = Url.builder()
                .originalUrl(originalUrl)
                .shortUrl(newShortUrl)
                .build();
        try {
            repository.save(entity);
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyExistsUrlException(originalUrl);
        }
        return new UrlDto(newShortUrl);
    }

    public String getOriginalUrl(String shortUrl) {
        return repository
                .findByShortUrl(shortUrl)
                .map(Url::getOriginalUrl)
                .orElseThrow(() -> new NoUrlException(shortUrl));
    }
}
