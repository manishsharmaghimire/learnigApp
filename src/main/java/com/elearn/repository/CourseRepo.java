package com.elearn.repository;

import com.elearn.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepo extends JpaRepository<Course, String> {
    Page<Course> findByLive(boolean live, Pageable pageable);
    
    boolean existsByTitleIgnoreCase(String title);
    
    List<Course> findByTitleContainingIgnoreCaseOrShortDescContainingIgnoreCase(String title, String shortDesc);
}
