package com.neighbour_snack.helper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.Normalizer;
import java.util.Random;

import org.springframework.web.multipart.MultipartFile;

public class AppUtil {

    private AppUtil() {
        // Private constructor to prevent instantiation
    }

    private static Random random = new Random();

    public static int generate6DigitRandomNumber() {
        return random.nextInt(900000) + 100000;
    }

    public static String saveFile(String uploadDir, String fileName, MultipartFile file) throws IOException {

        Path uploadPath = Paths.get(uploadDir);

        if (!(Files.exists(uploadPath))) {
            Files.createDirectories(uploadPath);
        }

        String fileCode = String.valueOf(generate6DigitRandomNumber());

        String finalImageName = fileCode + "-" + fileName;

        try (InputStream inputStream = file.getInputStream()) {
            Path filePath = uploadPath.resolve(finalImageName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING); // Override existing file with same
            // name
        } catch (IOException e) {
            throw new IOException("Could not save file : " + fileName, e);
        }

        return finalImageName;

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
