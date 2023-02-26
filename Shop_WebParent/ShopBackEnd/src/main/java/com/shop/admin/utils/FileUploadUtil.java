package com.shop.admin.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {

  private FileUploadUtil() {
  }

  public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
    Path uploadPath = Paths.get(uploadDir);
    folderCleaner(uploadPath);

    if (!Files.exists(uploadPath))
      Files.createDirectories(uploadPath);

    try (InputStream is = multipartFile.getInputStream()) {
      Path filePath = uploadPath.resolve(fileName);
      Files.copy(is, filePath, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      throw new IOException("Could not save file: " + fileName, e);
    }
  }

  public static void saveFileWithoutClearingForlder(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
    Path uploadPath = Paths.get(uploadDir);

    if (!Files.exists(uploadPath))
      Files.createDirectories(uploadPath);

    try (InputStream is = multipartFile.getInputStream()) {
      Path filePath = uploadPath.resolve(fileName);
      Files.copy(is, filePath, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      throw new IOException("Could not save file: " + fileName, e);
    }
  }

  public static void folderCleaner(Path uploadPath) {
    File[] listFiles = uploadPath.toFile().listFiles();
    if (listFiles != null) {
      for (File file : listFiles) {
        if (!file.isDirectory())
          file.delete();
      }
    }
  }


}
