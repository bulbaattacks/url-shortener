package io.github.bulbaattacks.url_shortener.repository;

import io.github.bulbaattacks.url_shortener.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlRepository extends JpaRepository<Url, Long> {
}
