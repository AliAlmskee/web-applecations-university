package com.main.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileServiceTest {

    private FileService fileService;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        fileService = new FileService();
        // Set the base path to the temp directory for testing
        ReflectionTestUtils.setField(fileService, "basePath", tempDir.toString());
    }

    @Test
    void testUploadFile() throws IOException {
        // Create a test file content
        String content = "This is a test file content";
        String fileName = "test-file.txt";
        MultipartFile multipartFile = new MockMultipartFile(
                "file",
                fileName,
                "text/plain",
                content.getBytes()
        );

        // Upload the file
        Path filePath = Path.of("uploads", "test");
        String savedPath = fileService.createFile(multipartFile, filePath);

        // Verify the file was saved
        assertNotNull(savedPath, "Saved path should not be null");
        assertTrue(savedPath.contains(fileName), "Saved path should contain the file name");
        
        // Verify the file exists on disk
        Path fullPath = tempDir.resolve(savedPath);
        assertTrue(Files.exists(fullPath), "File should exist on disk");
        
        // Verify the file content
        String fileContent = Files.readString(fullPath);
        assertEquals(content, fileContent, "File content should match");
    }

    @Test
    void testUploadFileCreatesDirectories() throws IOException {
        // Create a test file
        String content = "Test content";
        MultipartFile multipartFile = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                content.getBytes()
        );

        // Upload to a nested directory path
        Path filePath = Path.of("nested", "deep", "directory", "file.txt");
        String savedPath = fileService.createFile(multipartFile, filePath);

        // Verify directories were created
        Path fullPath = tempDir.resolve(savedPath);
        assertTrue(Files.exists(fullPath), "File should exist in nested directory");
        assertTrue(Files.exists(fullPath.getParent()), "Parent directories should be created");
    }

    @Test
    void testServeFile() throws Exception {
        // Create a test file manually
        Path testFilePath = tempDir.resolve("test-serve.txt");
        String content = "File to serve";
        Files.write(testFilePath, content.getBytes());

        // Serve the file
        File servedFile = fileService.getFile("test-serve.txt");

        // Verify the file is returned
        assertNotNull(servedFile, "Served file should not be null");
        assertTrue(servedFile.exists(), "Served file should exist");
        assertEquals("test-serve.txt", servedFile.getName(), "File name should match");
        
        // Verify file content
        String servedContent = Files.readString(servedFile.toPath());
        assertEquals(content, servedContent, "Served file content should match");
    }

    @Test
    void testServeFileWithNestedPath() throws Exception {
        // Create a nested directory structure
        Path nestedDir = tempDir.resolve("uploads").resolve("documents");
        Files.createDirectories(nestedDir);
        
        Path testFilePath = nestedDir.resolve("document.pdf");
        String content = "PDF content";
        Files.write(testFilePath, content.getBytes());

        // Serve the file using relative path
        File servedFile = fileService.getFile("uploads/documents/document.pdf");

        // Verify the file is returned
        assertNotNull(servedFile, "Served file should not be null");
        assertTrue(servedFile.exists(), "Served file should exist");
        assertEquals("document.pdf", servedFile.getName(), "File name should match");
    }

    @Test
    void testServeNonExistentFile() {
        // Try to serve a file that doesn't exist
        Exception exception = assertThrows(Exception.class, () -> {
            fileService.getFile("non-existent-file.txt");
        });

        // Verify the exception message
        assertTrue(exception.getMessage().contains("file does not exist"),
                "Exception should indicate file does not exist");
    }

    @Test
    void testUploadAndServeFile() throws Exception {
        // Upload a file
        String content = "Upload and serve test content";
        MultipartFile multipartFile = new MockMultipartFile(
                "file",
                "upload-serve.txt",
                "text/plain",
                content.getBytes()
        );

        Path filePath = Path.of("test");
        String savedPath = fileService.createFile(multipartFile, filePath);

        // Serve the uploaded file
        File servedFile = fileService.getFile(savedPath);

        // Verify the file can be served
        assertNotNull(servedFile, "Served file should not be null");
        assertTrue(servedFile.exists(), "Served file should exist");
        
        // Verify content matches
        String servedContent = Files.readString(servedFile.toPath());
        assertEquals(content, servedContent, "Served content should match uploaded content");
    }

    @Test
    void testUploadFileReplacesExisting() throws IOException {
        // Create initial file
        Path existingFile = tempDir.resolve("replace-test.txt");
        Files.write(existingFile, "Old content".getBytes());

        // Upload a new file with the same name
        String newContent = "New content";
        MultipartFile multipartFile = new MockMultipartFile(
                "file",
                "replace-test.txt",
                "text/plain",
                newContent.getBytes()
        );

        String savedPath = fileService.createFile(multipartFile, Path.of(""));

        // Verify the file was replaced
        Path fullPath = tempDir.resolve(savedPath);
        String fileContent = Files.readString(fullPath);
        assertEquals(newContent, fileContent, "File should be replaced with new content");
    }
}

