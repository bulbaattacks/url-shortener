package io.github.bulbaattacks.url_shortener.exception;

public class NoUrlException extends RuntimeException {
    public NoUrlException(String url) {
        super("No redirect found from this url: %s".formatted(url));
    }
}