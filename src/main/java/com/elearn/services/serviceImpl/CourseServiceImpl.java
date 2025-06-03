package com.elearn.services.serviceImpl;

import com.elearn.dto.CourseDto;
import com.elearn.dto.CustomPageResponse;
import com.elearn.dto.ResourceContentType;
import com.elearn.entity.Course;
import com.elearn.exception.ResourceNotFoundException;
import com.elearn.repository.CourseRepo;
import com.elearn.services.CategoryService;
import com.elearn.services.CourseService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private CourseRepo courseRepository;


    private ModelMapper modelMapper;


    private CategoryService categoryService;


   // private FileService fileService;

    /**
     * @param courseDto 
     * @return
     */
    @Override
    public CourseDto createCourse(CourseDto courseDto) {
        courseDto.setId(UUID.randomUUID().toString());
        courseDto.setCreatedDate(new Date());
        Course course = modelMapper.map(courseDto, Course.class);
        Course savedCourse = courseRepository.save(course);
        return modelMapper.map(savedCourse, CourseDto.class);
    }

    /**
     * @param courseId
     * @param courseDto
     * @return
     */
    @Override
    public CourseDto updateCourse(String courseId, CourseDto courseDto) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));;
        course.setTitle(courseDto.getTitle());
        course.setShortDesc(courseDto.getShortDesc());
        course.setLongDesc(courseDto.getLongDesc());
        course.setPrice(courseDto.getPrice());
        course.setDiscount(courseDto.getDiscount());
        course.setLive(courseDto.isLive());
        Course updatedCourse = courseRepository.save(course);
        return modelMapper.map(updatedCourse, CourseDto.class);
    }

    /**
     * @param id 
     * @return
     */
    @Override
    public CourseDto getCourseById(String id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return modelMapper.map(course, CourseDto.class);
    }


    @Override
    public CustomPageResponse<CourseDto> getAll(int pageNumber, int pageSize, String sortBy) {
        Sort sort= Sort.by(sortBy).ascending();
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Course> coursesList =this.courseRepository.findAll(pageable);
        List<Course> content = coursesList.getContent();
        List<CourseDto> contentList = content.stream().map(course -> modelMapper.map(course, CourseDto.class)).toList();
        CustomPageResponse<CourseDto> customPageResponse= new CustomPageResponse<>();
        customPageResponse.setContent(contentList);
        customPageResponse.setPageNumber(pageNumber);
        customPageResponse.setPageSize(pageSize);
        customPageResponse.setTotalPages(coursesList.getTotalPages());
        customPageResponse.setTotalElements(coursesList.getTotalElements());
        customPageResponse.setLast(coursesList.isLast());

        return customPageResponse;
    }


    @Override
    public CustomPageResponse<CourseDto> getAllCoursesLive(int pageNumber, int pageSize, String sortBy) {
        Sort sort = Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Course> liveCourse = courseRepository.findByLive(true, pageable);

        List<Course> content = liveCourse.getContent();
        List<CourseDto> contentList = content.stream()
                .map(course -> modelMapper.map(course, CourseDto.class))
                .toList();

        CustomPageResponse<CourseDto> customPageResponse = new CustomPageResponse<>();
        customPageResponse.setContent(contentList);
        customPageResponse.setPageNumber(pageNumber);
        customPageResponse.setPageSize(pageSize);
        customPageResponse.setTotalPages(liveCourse.getTotalPages());
        customPageResponse.setTotalElements(liveCourse.getTotalElements());
        customPageResponse.setLast(liveCourse.isLast());

        return customPageResponse;
    }


    /**
     * @param id 
     */
    @Override

    public void deleteCourse(String id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        courseRepository.delete(course);
    }


    /**
     * @param keyword 
     * @return
     */
    @Override
    public List<CourseDto> searchCourses(String keyword) {
        return List.of();
    }

    /**
     * @param file 
     * @param courseId
     * @return
     * @throws IOException
     */
    @Override
    public CourseDto saveBanner(MultipartFile file, String courseId) throws IOException {
        return null;
    }

    /**
     * @param courseId 
     * @return
     */
    @Override
    public ResourceContentType getCourseBannerById(String courseId) {
        return null;
    }
}
