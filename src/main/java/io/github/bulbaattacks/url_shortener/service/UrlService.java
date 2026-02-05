package io.github.bulbaattacks.url_shortener.service;

import io.github.bulbaattacks.url_shortener.entity.Url;
import io.github.bulbaattacks.url_shortener.dto.UrlDto;
import io.github.bulbaattacks.url_shortener.util.UrlHasher;
import io.github.bulbaattacks.url_shortener.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepository repository;

    public UrlDto shortUrl(UrlDto dto) {
        var originalUrl = dto.url();
        var uri = URI.create(originalUrl);
        var path = uri.getPath();
        var encodePath = UrlHasher.hashUrl(path);
        var shortUrl = uri.getHost() + "/" + encodePath;
        var entity = Url.builder()
                .originalUrl(originalUrl)
                .shortUrl(shortUrl)
                .build();
        repository.save(entity);
        return new UrlDto(shortUrl);
    }
}
