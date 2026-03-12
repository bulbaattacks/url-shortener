package io.github.bulbaattacks.url_shortener.controller;

import io.github.bulbaattacks.url_shortener.dto.UrlDto;
import io.github.bulbaattacks.url_shortener.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UrlController {

    private final UrlService service;

    @PostMapping("/short")
    public UrlDto urlShortener(@RequestBody UrlDto dto) {
        return service.createShortUrl(dto);
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String shortUrl) {
        var originalUrl = service.getOriginalUrl(shortUrl);
        return ResponseEntity
                .status(302)
                .header("Location", originalUrl)
                .build();
    }
}
