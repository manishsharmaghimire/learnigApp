package com.elearn.services.serviceImpl;

import com.elearn.dto.CustomPageResponse;
import com.elearn.dto.VideoDto;
import com.elearn.entity.Course;
import com.elearn.entity.Video;
import com.elearn.exception.ResourceNotFoundException;
import com.elearn.repository.CourseRepo;
import com.elearn.repository.VideoRepository;
import com.elearn.services.VideoService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

    private static final Logger logger = LoggerFactory.getLogger(VideoServiceImpl.class);

    private final VideoRepository videoRepository;
    private final CourseRepo courseRepo;
    private final ModelMapper modelMapper;

    /**
     * Creates a new video and associates it with a course.
     *
     * @param videoDto the video data
     * @return the created video DTO
     */
    @Override
    public VideoDto createVideo(VideoDto videoDto) {
        logger.info("Creating a new video");
        videoDto.setId(UUID.randomUUID().toString());
        Video video = modelMapper.map(videoDto, Video.class);

        Course course = courseRepo.findById(videoDto.getCourse().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + videoDto.getCourse().getId()));

        course.getVideos().add(video);
        video.setCourse(course);
        Video savedVideo = videoRepository.save(video);
        logger.info("Video created with ID: {}", savedVideo.getId());
        return modelMapper.map(savedVideo, VideoDto.class);
    }

    /**
     * Updates an existing video.
     *
     * @param videoId  the ID of the video to update
     * @param videoDto the updated video data
     * @return the updated video DTO
     */
    @Override
    public VideoDto updateVideo(String videoId, VideoDto videoDto) {
        logger.info("Updating video with ID: {}", videoId);
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new ResourceNotFoundException("Video not found with id " + videoId));

        video.setTitle(videoDto.getTitle());
        video.setDescription(videoDto.getDesc());
        video.setFilePath(videoDto.getFilePath());
        video.setContentType(videoDto.getContentType());

        Course course = courseRepo.findById(videoDto.getCourse().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + videoDto.getCourse().getId()));
        video.setCourse(course);

        Video updatedVideo = videoRepository.save(video);
        logger.info("Video updated with ID: {}", updatedVideo.getId());
        return modelMapper.map(updatedVideo, VideoDto.class);
    }

    /**
     * Retrieves a video by its ID.
     *
     * @param videoId the ID of the video
     * @return the video DTO
     */
    @Override
    public VideoDto getVideoById(String videoId) {
        logger.info("Fetching video with ID: {}", videoId);
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new ResourceNotFoundException("Video not found with id " + videoId));
        return modelMapper.map(video, VideoDto.class);
    }

    /**
     * Retrieves all videos with pagination and sorting.
     *
     * @param pageNumber page number
     * @param pageSize   size of the page
     * @param sortBy     sorting parameter
     * @return paginated response of video DTOs
     */
    @Override
    public CustomPageResponse<VideoDto> getAllVideos(int pageNumber, int pageSize, String sortBy) {
        logger.info("Fetching all videos - page: {}, size: {}, sortBy: {}", pageNumber, pageSize, sortBy);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        Page<Video> videoPage = videoRepository.findAll(pageable);

        List<VideoDto> contentList = videoPage.getContent().stream()
                .map(video -> modelMapper.map(video, VideoDto.class))
                .collect(Collectors.toList());

        return CustomPageResponse.<VideoDto>builder()
                .pageNumber(videoPage.getNumber())
                .pageSize(videoPage.getSize())
                .totalElements(videoPage.getTotalElements())
                .totalPages(videoPage.getTotalPages())
                .isLast(videoPage.isLast())
                .content(contentList)
                .build();

    }

    /**
     * Deletes a video by its ID.
     *
     * @param videoId the ID of the video
     */
    @Override
    public void deleteVideo(String videoId) {
        logger.info("Deleting video with ID: {}", videoId);
        videoRepository.deleteById(videoId);
    }

    /**
     * Searches videos by keyword in title or description.
     *
     * @param keyword the search keyword
     * @return list of matching video DTOs
     */
    @Override
    public List<VideoDto> searchVideos(String keyword) {
        logger.info("Searching videos with keyword: {}", keyword);
        List<Video> videos = videoRepository.searchByTitleOrDescription(keyword);
        return videos.stream()
                .map(video -> modelMapper.map(video, VideoDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all videos associated with a specific course.
     *
     * @param courseId the ID, of course
     * @return list of video DTOs belonging to the course
     */
    @Override
    public List<VideoDto> getVideoOfCourse(String courseId) {
        logger.info("Fetching videos for course ID: {}", courseId);
        List<Video> videos = videoRepository.findByCourseId(courseId);
        return videos.stream()
                .map(video -> modelMapper.map(video, VideoDto.class))
                .collect(Collectors.toList());
    }
}