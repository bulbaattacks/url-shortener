package io.github.bulbaattacks.url_shortener.service;

import io.github.bulbaattacks.url_shortener.dto.UrlDto;
import io.github.bulbaattacks.url_shortener.entity.Url;
import io.github.bulbaattacks.url_shortener.exception.NoUrlException;
import io.github.bulbaattacks.url_shortener.repository.UrlRepository;
import io.github.bulbaattacks.url_shortener.util.UrlHasher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepository repository;

    public UrlDto createShortUrl(UrlDto dto) {
        var originalUrl = dto.url();
        var optUrl = repository.findByOriginalUrl(originalUrl);
        if (optUrl.isPresent()) {
            var existedShortUrl = optUrl.get().getShortUrl();
            return new UrlDto(existedShortUrl);
        }
        var newShortUrl = UrlHasher.hashUrl(originalUrl);
        var entity = Url.builder()
                .originalUrl(originalUrl)
                .shortUrl(newShortUrl)
                .build();
        repository.save(entity);
        return new UrlDto(newShortUrl);
    }

    public String getOriginalUrl(String shortUrl) {
        return repository
                .findByShortUrl(shortUrl)
                .map(Url::getOriginalUrl)
                .orElseThrow(() -> new NoUrlException(shortUrl));
    }
}
