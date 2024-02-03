package org.example.springsecurity.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@Getter
@Setter
public class MultipartFileCustom implements MultipartFile {
    private String fileName;
    private String originalFilename;
    private String contentType;
    private boolean isEmpty;
    private long size;
    private byte[] bytes;
    private InputStream inputStream;
    private File files;

    public MultipartFileCustom(MultipartFile fileName) throws IOException {
        this.bytes = fileName.getBytes();
        this.fileName = fileName.getName();
        this.originalFilename = setOriginalFilename(Objects.requireNonNull(fileName.getOriginalFilename()));
        this.contentType = fileName.getContentType();
        this.isEmpty = fileName.isEmpty();
        this.size = fileName.getSize();
        this.inputStream = fileName.getInputStream();
    }

    @Override
    @NonNull
    public String getName() {
        return fileName;
    }

    @Override
    public String getOriginalFilename() {
        if (hasExtension(originalFilename.toLowerCase(), ".jpg", ".png", "pdf")) {
            return originalFilename;
        } else {
            return originalFilename.concat(detectFileTypeByContent(bytes));
        }
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return isEmpty;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    @NonNull
    public byte[] getBytes() throws IOException {
        return bytes;
    }

    @Override
    @NonNull
    public InputStream getInputStream() throws IOException {
        return inputStream;
    }

    @Override
    public void transferTo(@NonNull File dest) throws IOException, IllegalStateException {
        FileCopyUtils.copy(bytes, dest);
    }

    public String setOriginalFilename(String originalFilename) {
        if (hasExtension(originalFilename.toLowerCase(), ".jpg", ".png", "pdf")) {
            return originalFilename;
        } else {
            return originalFilename + detectFileTypeByContent(bytes).toLowerCase();
        }
    }

    public static String detectFileTypeByContent(byte[] bytes) {
        try {
            // Kiểm tra các byte đầu tiên để xác định loại file
            if (bytes.length >= 2 && (bytes[0] == (byte) 0xFF && bytes[1] == (byte) 0xD8 || bytes[0] == (byte) 0xFF && bytes[1] == (byte) 0xD9)) {
                return "JPEG"; // JPG & JPEG
            } else if (bytes.length >= 4 && bytes[0] == (byte) 0x25 && bytes[1] == (byte) 0x50 && bytes[2] == (byte) 0x44 && bytes[3] == (byte) 0x46) {
                return "PDF";
            } else if (bytes.length >= 8 && bytes[0] == (byte) 0x89 && bytes[1] == (byte) 0x50 && bytes[2] == (byte) 0x4E && bytes[3] == (byte) 0x47 && bytes[4] == (byte) 0x0D && bytes[5] == (byte) 0x0A && bytes[6] == (byte) 0x1A && bytes[7] == (byte) 0x0A) {
                return "PNG";
            }
            return "Unknown";
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return "Unknown";
        }
    }

    private static boolean hasExtension(String filename, String... extensions) {
        if (filename == null) {
            return false;
        }
        String lowerCaseFilename = filename.toLowerCase();

        for (String extension : extensions) {
            if (lowerCaseFilename.endsWith(extension)) {
                return true;
            }
        }

        return false;
    }
}
