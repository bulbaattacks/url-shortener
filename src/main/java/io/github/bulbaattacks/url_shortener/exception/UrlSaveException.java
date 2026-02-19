package io.github.bulbaattacks.url_shortener.exception;

public class AlreadyExistsUrlException extends RuntimeException {
    public AlreadyExistsUrlException(String url) {
        super("Short url already exists for this url: %s".formatted(url));
    }
}