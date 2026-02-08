package io.github.bulbaattacks.url_shortener.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NoUrlException extends ResponseStatusException {
    public static final String MSG = "No redirect found from this url: %s";

    public NoUrlException(String url) {
        super(HttpStatus.BAD_REQUEST, MSG.formatted(url));
    }
}