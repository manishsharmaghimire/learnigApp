package com.elearn.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    boolean deleteCourseBannerIfExists(String path);
    String save(MultipartFile file, String outputPath, String filename) throws IOException;
}
