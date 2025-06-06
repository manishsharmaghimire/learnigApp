package com.elearn.services.serviceImpl;

import com.elearn.dto.VideoDto;
import com.elearn.dto.VideoUploadResponse;
import com.elearn.entity.Video;
import com.elearn.repository.VideoRepository;
import com.elearn.services.VideoUploadService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@RequiredArgsConstructor
public class VideoUploadServiceImpl implements VideoUploadService {

    private final VideoRepository videoRepo;
    private final ModelMapper modelMapper;

    @Value("${video.upload.path}")
    private String uploadPath;

    @Override
    public VideoUploadResponse uploadVideo(String videoId, MultipartFile videoFile) {
        VideoUploadResponse videoUploadResponse = new VideoUploadResponse();

        // 1. Validate empty file
        if (videoFile.isEmpty()) {
            videoUploadResponse.setMessage("File is empty !!");
            videoUploadResponse.setSuccess(false);
            return videoUploadResponse;
        }

        // 2. Validate content type
        String contentType = videoFile.getContentType();
        if (contentType == null || !contentType.startsWith("video/")) {
            videoUploadResponse.setMessage("Invalid File !!");
            videoUploadResponse.setSuccess(false);
            return videoUploadResponse;
        }

        try {
            // 3. Create directory if not exists
            Path path = Paths.get(uploadPath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            String filename = System.currentTimeMillis() + "_" + videoFile.getOriginalFilename();
            Path mainPath = path.resolve(filename);
            Video video = videoRepo.findById(videoId).orElseThrow(() -> new RuntimeException("Video metadata not found !!"));

            String oldFilepath = video.getFilePath();
            if(oldFilepath !=null){
                Path OldFileObjectPath = Paths.get(oldFilepath);
                if(Files.exists(OldFileObjectPath)){

                }
            }


            Files.copy(videoFile.getInputStream(),mainPath, StandardCopyOption.REPLACE_EXISTING);
            video.setFilePath(mainPath.toString());
            video.setContentType(contentType);
            videoRepo.save(video);
            videoUploadResponse.setMessage("Video File uploaded Successfully");
            ///ENCODE VIDEO METHOD: VIDEO.
            videoUploadResponse.setSuccess(true);
            videoUploadResponse.setVideoDto(modelMapper.map(video, VideoDto.class));
            return videoUploadResponse;



        } catch (IOException e) {
            videoUploadResponse.setMessage("File upload failed: " + e.getMessage());
            videoUploadResponse.setSuccess(false);
        }

        return videoUploadResponse;
    }
}
