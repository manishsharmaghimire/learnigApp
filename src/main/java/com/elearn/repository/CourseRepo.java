package com.elearn.repository;


import com.elearn.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepo extends JpaRepository<Course,String> {
    Page<Course> findByLive(boolean live, Pageable pageable);

}
