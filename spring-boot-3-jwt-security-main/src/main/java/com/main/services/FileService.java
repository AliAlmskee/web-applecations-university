package com.main.services;

import org.springframework.stereotype.Component;
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


@Component
@Service
public class FileService {
    private static final String basePath = "storage/";
    public String createFile(MultipartFile file, Path path) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        InputStream inputStream = file.getInputStream();
        Path filePath = path.resolve(fileName);
        Path fullPath = Paths.get(basePath, filePath.toString());
        if (Files.notExists(fullPath)) {
            Files.createDirectories(fullPath);
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

