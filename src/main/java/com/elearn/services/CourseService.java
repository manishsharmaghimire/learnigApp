package com.elearn.services;

import com.elearn.dto.CourseDto;
import com.elearn.dto.CustomPageResponse;
import com.elearn.dto.ResourceContentType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CourseService {

    CourseDto createCourse(CourseDto courseDto);

    CourseDto updateCourse(String courseId, CourseDto courseDto);

    CourseDto getCourseById(String id);

    public CustomPageResponse<CourseDto> getAll(int pageNumber, int pageSize, String sortBy);

    CustomPageResponse<CourseDto> getAllCoursesLive(int pageNumber, int pageSize, String sortBy);

    void deleteCourse(String id);

    List<CourseDto> searchCourses(String keyword);

    CourseDto saveBanner(MultipartFile file, String courseId) throws IOException;

    ResourceContentType getCourseBannerById(String courseId);
}
