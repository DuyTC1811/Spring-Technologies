package org.example.ssestreaming.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Optional;

public class FileUtils {

    public static File convertToFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("MultipartFile is null or empty");
        }

        try {
            String prefix = Optional.ofNullable(file.getOriginalFilename())
                    .map(name -> name.replaceAll("\\W+", ""))
                    .filter(name -> name.length() >= 3)
                    .orElse("upload");

            Path tempFile = Files.createTempFile(prefix, null);
            file.transferTo(tempFile);
            return tempFile.toFile();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to convert MultipartFile to File", e);
        }
    }

    public static String fileToBase64(File file) {
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("File is null or does not exist");
        }

        try {
            byte[] fileBytes = Files.readAllBytes(file.toPath());
            return Base64.getEncoder().encodeToString(fileBytes);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to convert file to Base64", e);
        }
    }

}
