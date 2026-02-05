package io.github.bulbaattacks.url_shortener.util;

import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class UrlHasher {

    public static String hashUrl(String url) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(url.getBytes(StandardCharsets.UTF_8));
            long value = ByteBuffer.wrap(hash).getLong();
            value = Math.abs(value);
            return Encoder.encode(value);
        } catch (Exception e) {
            throw new RuntimeException("Hash generation failed", e);
        }
    }
}

