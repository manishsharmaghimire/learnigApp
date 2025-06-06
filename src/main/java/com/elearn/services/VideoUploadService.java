package com.elearn.services;

import com.elearn.dto.VideoUploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface VideoUploadService {


    VideoUploadResponse uploadVideo( String videoId, MultipartFile file);
}
