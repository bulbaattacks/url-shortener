package io.github.bulbaattacks.url_shortener.exception;

public class HashGenerationException extends RuntimeException {
    public HashGenerationException() {
        super("Hash generation failed");
    }
}