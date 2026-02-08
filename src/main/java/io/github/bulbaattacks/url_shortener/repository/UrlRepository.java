package io.github.bulbaattacks.url_shortener.repository;

import io.github.bulbaattacks.url_shortener.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UrlRepository extends JpaRepository<Url, Long> {
    @Query("select u from Url u where shortUrl = :shortUrl")
    Optional<Url> findByShortUrl(String shortUrl);

    @Query("select u from Url u where originalUrl = :originalUrl")
    Optional<Url> findByOriginalUrl(String originalUrl);
}
