package io.github.bulbaattacks.url_shortener.exception;

public class UrlSaveException extends RuntimeException {
    public UrlSaveException(String url) {
        super("Can't save url: %s".formatted(url));
    }
}