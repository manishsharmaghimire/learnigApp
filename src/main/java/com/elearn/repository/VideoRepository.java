package com.elearn.repository;

import com.elearn.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video,String> {
    @Query("SELECT v FROM Video v WHERE LOWER(v.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(v.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Video> searchByTitleOrDescription(@Param("keyword") String keyword);


    List<Video> findByCourseId(String courseId);
}
