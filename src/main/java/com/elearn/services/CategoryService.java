package com.elearn.services;

import com.elearn.dto.CategoryDto;
import com.elearn.dto.CourseDto;
import com.elearn.dto.CustomPageResponse;

import java.util.List;

public interface CategoryService{
    CategoryDto insert(CategoryDto categoryDto);
    CustomPageResponse<CategoryDto> getAll(int pageNumber, int pageSize, String sortBy);
    CategoryDto get(String categoryId);
    void delete(String categoryId);
    CategoryDto update(CategoryDto categoryDto, String categoryId);
    void addCourseToCategory(String catId, String courseId);
    List<CourseDto> getCoursesOfCat(String categoryId);

}
