package com.main.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class FileService {

    @Value("${file.storage.path:${user.home}/app-storage}")
    private String basePath;

    public String createFile(MultipartFile file, Path path) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        InputStream inputStream = file.getInputStream();
        Path filePath = path.resolve(fileName);
        Path fullPath = Paths.get(basePath, filePath.toString());
        Path parentDir = fullPath.getParent();
        if (parentDir != null && Files.notExists(parentDir)) {
            Files.createDirectories(parentDir);
        }
        Files.copy(inputStream, fullPath, StandardCopyOption.REPLACE_EXISTING);
        return filePath.toString().replace("\\", "/");
    }

    public File getFile(String path) throws Exception {
        Path fullPath = Paths.get(basePath, path);
        File file = fullPath.toFile();
        if(!file.exists()){
            throw new Exception("file does not exist");
        }
        return file;
    }
}

