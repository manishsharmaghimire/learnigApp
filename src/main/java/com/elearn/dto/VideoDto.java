package com.elearn.dto;

import com.elearn.entity.Course;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VideoDto {
    private String id;

    private String title;

    private String desc;

    private String filePath;

    private String contentType;

    private Course course;
}
