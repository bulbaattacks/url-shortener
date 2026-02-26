package io.github.bulbaattacks.url_shortener.repository;

import io.github.bulbaattacks.url_shortener.entity.Url;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Testcontainers
@DataJpaTest
class UrlRepositoryTest {

    private String shortUrl = "abc123";
    private String originalUrl = "https://example.com";

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
    }

    @Autowired
    private UrlRepository repository;

    @Test
    void createShortUrl_shouldSaveAndReturnShortUrl_whenOriginalUrlNotExists() {
        var entity = createEntity(shortUrl, originalUrl);
        repository.save(entity);

        assertEquals(1, repository.count());
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void dontCreateShortUrl_shouldReturnException_whenOriginalUrlExists() {
        var entity1 = createEntity(shortUrl, originalUrl);
        var entity2 = createEntity(shortUrl, originalUrl);

        repository.save(entity1);
        assertThrows(DataIntegrityViolationException.class, () -> repository.save(entity2));

        assertEquals(1, repository.count());

        repository.deleteAll();
    }

    @Test
    void delete_shouldRemoveEntity() {
        var entity = createEntity(shortUrl, originalUrl);
        repository.delete(entity);

        assertEquals(0, repository.count());
    }

    private Url createEntity(String shortUrl, String originalUrl) {
        return Url.builder()
                .shortUrl(shortUrl)
                .originalUrl(originalUrl)
                .build();
    }
}

