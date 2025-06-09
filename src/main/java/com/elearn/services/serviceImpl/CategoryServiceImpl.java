package com.elearn.services.serviceImpl;

import com.elearn.dto.CategoryDto;
import com.elearn.dto.CourseDto;
import com.elearn.dto.CustomPageResponse;
import com.elearn.entity.Category;
import com.elearn.entity.Course;
import com.elearn.exception.ResourceNotFoundException;
import com.elearn.repository.CategoryRepository;
import com.elearn.repository.CourseRepo;
import com.elearn.services.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Service implementation for handling Category-related business logic.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CourseRepo courseRepo;
    private final ModelMapper modelMapper;

    /**
     * Inserts a new category into the system.
     *
     * @param categoryDto the data of the category to insert
     * @return the saved category with generated ID and date
     */
    @Override
    public CategoryDto insert(CategoryDto categoryDto) {
        String catId = UUID.randomUUID().toString();
        categoryDto.setId(catId);
        categoryDto.setAddedDate(new Date());
        Category category = modelMapper.map(categoryDto, Category.class);
        Category savedCat = categoryRepository.save(category);
        log.info("Category inserted with ID: {}", catId);
        return modelMapper.map(savedCat, CategoryDto.class);
    }

    /**
     * Retrieves all categories with pagination and sorting.
     *
     * @param pageNumber the page number
     * @param pageSize   the size of the page
     * @param sortBy     the field to sort by
     * @return paginated custom response of category DTOs
     */
    @Override
    public CustomPageResponse<CategoryDto> getAll(int pageNumber, int pageSize, String sortBy) {
        Sort sort = Sort.by(sortBy).ascending();
        Pageable pageRequest = PageRequest.of(pageNumber, pageSize, sort);
        Page<Category> categoryPage = categoryRepository.findAll(pageRequest);
        List<CategoryDto> categoryDtoList = categoryPage.getContent()
                .stream()
                .map(cat -> modelMapper.map(cat, CategoryDto.class))
                .toList();

        CustomPageResponse<CategoryDto> response = new CustomPageResponse<>();
        response.setContent(categoryDtoList);
        response.setLast(categoryPage.isLast());
        response.setTotalElements(categoryPage.getTotalElements());
        response.setTotalPages(categoryPage.getTotalPages());
        response.setPageNumber(pageNumber);
        response.setPageSize(categoryPage.getSize());

        log.info("Fetched page {} of categories with size {}", pageNumber, pageSize);
        return response;
    }

    /**
     * Retrieves a specific category by ID.
     *
     * @param categoryId the ID of the category
     * @return the category DTO
     * @throws ResourceNotFoundException if category not found
     */
    @Override
    public CategoryDto get(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
        log.info("Category found with ID: {}", categoryId);
        return modelMapper.map(category, CategoryDto.class);
    }

    /**
     * Deletes a category by ID.
     *
     * @param categoryId the ID of the category
     * @throws ResourceNotFoundException if category not found
     */
    @Override
    public void delete(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
        categoryRepository.delete(category);
        log.warn("Category deleted with ID: {}", categoryId);
    }

    /**
     * Updates an existing category.
     *
     * @param categoryDto the new data
     * @param categoryId  the ID of the category to update
     * @return updated category DTO
     */
    @Override
    public CategoryDto update(CategoryDto categoryDto, String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription()); // fix: previously set to getId()
        Category savedCategory = categoryRepository.save(category);
        log.info("Category updated with ID: {}", categoryId);
        return modelMapper.map(savedCategory, CategoryDto.class);
    }

    /**
     * Adds a course to an existing category.
     *
     * @param catId    the category ID
     * @param courseId the course ID to add
     */
    @Override
    @Transactional
    public void addCourseToCategory(String catId, String courseId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + catId));
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

        category.addCourse(course);
        categoryRepository.save(category);
        log.info("Course with ID {} added to category ID {}", courseId, catId);
    }

    /**
     * Gets all courses under a specific category.
     *
     * @param categoryId the category ID
     * @return list of course DTOs
     */
    @Override
    public List<CourseDto> getCoursesOfCat(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category Not found with ID: " + categoryId));
        List<Course> courses = category.getCourses();
        log.info("Fetched {} courses under category ID {}", courses.size(), categoryId);
        return courses.stream().map(course -> modelMapper.map(course, CourseDto.class)).toList();
    }
}
