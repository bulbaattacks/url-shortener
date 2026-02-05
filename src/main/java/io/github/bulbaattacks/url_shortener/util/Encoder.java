package io.github.bulbaattacks.url_shortener.util;

public class Encoder {

    private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = ALPHABET.length();

    public static String encode(long id) {
        StringBuilder sb = new StringBuilder();

        while (id > 0) {
            int index = (int) (id % BASE);
            sb.append(ALPHABET.charAt(index));
            id /= BASE;
        }

        return sb.reverse().toString();
    }
}
