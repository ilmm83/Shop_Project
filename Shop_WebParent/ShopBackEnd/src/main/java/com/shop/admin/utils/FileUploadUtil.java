package com.shop.admin.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadUtil {

    private FileUploadUtil() {
    }


    public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
        var uploadPath = Paths.get(uploadDir);
        folderCleaner(uploadPath);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (var is = multipartFile.getInputStream()) {
            var filePath = uploadPath.resolve(fileName);
            Files.copy(is, filePath, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            throw new FileNotSavedException("Could not save file: " + fileName, e);
        }
    }

    public static void saveFileWithoutClearingFolder(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
        var uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream is = multipartFile.getInputStream()) {
            var filePath = uploadPath.resolve(fileName);
            Files.copy(is, filePath, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            throw new IOException("Could not save file: " + fileName, e);
        }
    }

    public static void folderCleaner(Path uploadPath) {
        var listFiles = uploadPath.toFile().listFiles();

        if (listFiles != null) {
            for (var file : listFiles) {
                if (!file.isDirectory()) {
                    file.delete();
                }
            }
        }
    }
}
