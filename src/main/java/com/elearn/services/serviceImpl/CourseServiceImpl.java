package com.elearn.services.serviceImpl;


import com.elearn.dto.CourseDto;
import com.elearn.dto.CustomPageResponse;
import com.elearn.dto.ResourceContentType;
import com.elearn.entity.Course;
import com.elearn.exception.DuplicateResourceException;
import com.elearn.exception.InvalidInputException;
import com.elearn.exception.ResourceNotFoundException;
import com.elearn.repository.CourseRepo;
import com.elearn.services.CategoryService;
import com.elearn.services.CourseService;
import com.elearn.services.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import com.elearn.config.AppConstants;

import com.elearn.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of {@link CourseService} providing course management functionality.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseServiceImpl implements CourseService {


    private final CourseRepo courseRepository;
    private final ModelMapper modelMapper;
    private final CategoryService categoryService;
    private final FileService fileService;

    @Override
    @Transactional
    public CourseDto createCourse(CourseDto courseDto) {
        log.info("Creating new course with title: {}", courseDto.getTitle());
        
        // Business validation: Check for duplicate title
        if (courseRepository.existsByTitleIgnoreCase(courseDto.getTitle())) {
            throw new DuplicateResourceException("Course with title " + courseDto.getTitle() + " already exists");
        }

        Course course = modelMapper.map(courseDto, Course.class);
        course.setId(UUID.randomUUID().toString());
        course.setCreatedDate(new Date());
        course.setLastModifiedDate(new Date());
        
        Course savedCourse = courseRepository.save(course);
        log.info("Created course with id: {}", savedCourse.getId());
        
        return modelMapper.map(savedCourse, CourseDto.class);
    }

    @Override
    @Transactional
    public CourseDto updateCourse(String courseId, CourseDto courseDto) {
        log.info("Updating course with id: {}", courseId);
        
        // Find existing course
        Course existingCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

        // Business validation: Check for duplicate title if it's being changed
        if (!existingCourse.getTitle().equalsIgnoreCase(courseDto.getTitle()) && 
            courseRepository.existsByTitleIgnoreCase(courseDto.getTitle())) {
            throw new DuplicateResourceException("Course with title " + courseDto.getTitle() + " already exists");
        }

        modelMapper.map(courseDto, existingCourse);
        existingCourse.setLastModifiedDate(new Date());
        
        Course updatedCourse = courseRepository.save(existingCourse);
        log.info("Updated course with id: {}", courseId);
        
        return modelMapper.map(updatedCourse, CourseDto.class);
    }

    @Override
    public CourseDto getCourseById(String id) {
        log.debug("Fetching course with id: {}", id);
        
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
                
        return modelMapper.map(course, CourseDto.class);
    }

    @Override
    public CustomPageResponse<CourseDto> getAll(int pageNumber, int pageSize, String sortBy) {
        log.debug("Fetching all courses - page: {}, size: {}, sortBy: {}", pageNumber, pageSize, sortBy);
        
        String sortField = (sortBy != null && !sortBy.trim().isEmpty()) ? sortBy : "title";
        Sort sort = Sort.by(sortField).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        
        Page<Course> coursesPage = courseRepository.findAll(pageable);
        
        return buildCustomPageResponse(coursesPage, pageNumber, pageSize);
    }

    @Override
    public CustomPageResponse<CourseDto> getAllCoursesLive(int pageNumber, int pageSize, String sortBy) {
        log.debug("Fetching all live courses - page: {}, size: {}, sortBy: {}", pageNumber, pageSize, sortBy);
        
        String sortField = (sortBy != null && !sortBy.trim().isEmpty()) ? sortBy : "title";
        Sort sort = Sort.by(sortField).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        
        Page<Course> liveCourses = courseRepository.findByLive(true, pageable);
        
        return buildCustomPageResponse(liveCourses, pageNumber, pageSize);
    }

    @Override
    @Transactional
    public void deleteCourse(String id) {
        log.info("Deleting course with id: {}", id);
        
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Course not found with id: " + id);
        }
        
        courseRepository.deleteById(id);
        log.info("Deleted course with id: {}", id);
    }

    @Override
    public List<CourseDto> searchCourses(String keyword) {
        log.debug("Searching courses with keyword: {}", keyword);
        
        if (!StringUtils.hasText(keyword) || keyword.length() < 3) {
            throw new InvalidInputException("Search keyword must be at least 3 characters long");
        }
        
        return courseRepository.findByTitleContainingIgnoreCaseOrShortDescContainingIgnoreCase(keyword, keyword)
                .stream()
                .map(course -> modelMapper.map(course, CourseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CourseDto saveBanner(MultipartFile file, String courseId) throws IOException {
        log.info("Saving banner for course id: {}", courseId);
        
        validateFile(file);
        
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

        // Delete old banner if exists
        if (course.getBanner() != null) {
            fileService.deleteCourseBannerIfExists(course.getBanner());
        }

        String filePath = fileService.save(
            file, 
            AppConstants.Paths.COURSE_BANNER_UPLOAD_DIR, 
            "banner_" + courseId + "." + getFileExtension(Objects.requireNonNull(file.getOriginalFilename()))
        );

        course.setBanner(filePath);
        course.setBannerContentType(file.getContentType());
        course.setLastModifiedDate(new Date());
        
        Course updatedCourse = courseRepository.save(course);
        log.info("Saved banner for course id: {}", courseId);
        
        return modelMapper.map(updatedCourse, CourseDto.class);
    }

    @Override
    public ResourceContentType getCourseBannerById(String courseId) {
        log.debug("Fetching banner for course id: {}", courseId);
        
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));
                
        if (course.getBanner() == null) {
            throw new ResourceNotFoundException("Banner not found for course id: " + courseId);
        }
        
        Path path = Paths.get(course.getBanner());
        Resource resource = new FileSystemResource(path);
        
        if (!resource.exists()) {
            throw new ResourceNotFoundException("Banner file not found for course id: " + courseId);
        }
        
        ResourceContentType contentType = new ResourceContentType();
        contentType.setResource(resource);
        contentType.setContentType(course.getBannerContentType());
        
        return contentType;
    }
    
    // Private helper methods
    
    private void validateFile(MultipartFile file) {
        ValidationUtil.validateFile(file);
    }
    
    private String getFileExtension(String filename) {
        int lastDot = filename.lastIndexOf('.');
        return lastDot == -1 ? "" : filename.substring(lastDot + 1);
    }
    
    private CustomPageResponse<CourseDto> buildCustomPageResponse(Page<Course> page, int pageNumber, int pageSize) {
        List<CourseDto> content = page.getContent().stream()
                .map(course -> modelMapper.map(course, CourseDto.class))
                .collect(Collectors.toList());
                
        CustomPageResponse<CourseDto> response = new CustomPageResponse<>();
        response.setContent(content);
        response.setPageNumber(pageNumber);
        response.setPageSize(pageSize);
        response.setTotalPages(page.getTotalPages());
        response.setTotalElements(page.getTotalElements());
        response.setLast(page.isLast());
        
        return response;
    }
}
