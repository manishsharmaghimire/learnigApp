package com.elearn.services.serviceImpl;

import com.elearn.services.FileService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileServiceImpl implements FileService {
    /**
     * @param path 
     * @return
     */
    @Override
    public boolean deleteCourseBannerIfExists(String path) {

        Path filePath = Paths.get(path);
        if (Files.exists(filePath)) {

            try {
                Files.delete(filePath);
                return true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

       return false;
    }

    /**
     * @param file 
     * @param outputPath
     * @param filename
     * @return
     * @throws IOException
     */
    @Override

    public String save(MultipartFile file, String outputPath, String filename) throws IOException {
        // Validate empty file
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty or null.");
        }

        // Validate content type (you can customize this list)
        String contentType = file.getContentType();
        if (contentType == null || !isAllowedContentType(contentType)) {
            throw new IllegalArgumentException("Unsupported file type: " + contentType);
        }

        // Optional: Validate file size (e.g., max 10MB)
        long maxSizeInBytes = 10 * 1024 * 1024;
        if (file.getSize() > maxSizeInBytes) {
            throw new IllegalArgumentException("File size exceeds maximum limit (10MB).");
        }

        // Create directory if it doesn't exist
        Path directoryPath = Paths.get(outputPath);
        Files.createDirectories(directoryPath);

        // Use provided filename or fallback to original filename with timestamp
        String cleanFileName = (filename != null && !filename.isEmpty())
                ? filename
                : System.currentTimeMillis() + "_" + file.getOriginalFilename();

        Path filePath = directoryPath.resolve(cleanFileName);

        // Save the file
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filePath.toString();
    }

    private boolean isAllowedContentType(String contentType) {
        return contentType.startsWith("image/") ||
                contentType.startsWith("video/") ||
                contentType.equals("application/pdf");
    }}

