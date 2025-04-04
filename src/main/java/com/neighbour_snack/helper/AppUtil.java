package com.neighbour_snack.helper;

import java.text.Normalizer;
import java.util.Base64;
import java.util.Random;

public class AppUtil {

    private AppUtil() {
        // Private constructor to prevent instantiation
    }

    private static Random random = new Random();

    public static int generate6DigitRandomNumber() {
        return random.nextInt(900000) + 100000;
    }

    public static String encode(String input) {
        return Base64.getEncoder().encodeToString(input.getBytes());
    }

    // Method to decode a string
    public static String decode(String encoded) {
        return new String(Base64.getDecoder().decode(encoded));
    }

    public static String normalizeName(String name) {
        // Convert to lowercase
        String normalized = name.toLowerCase();
        // Remove accents/diacritics
        normalized = Normalizer.normalize(normalized, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("\\p{M}", "");
        // Replace spaces and special characters with hyphens
        normalized = normalized.replaceAll("[^a-z0-9]+", "-");
        // Remove leading/trailing hyphens
        normalized = normalized.replaceAll("^-+|-+$", "");
        return normalized;
    }
}
