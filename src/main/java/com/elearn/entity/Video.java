package com.elearn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "videos")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Video {

    @Id
    @Column(name = "id", length = 100)
    private String id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Lob
    @Column(name = "description", nullable = true)
    private String desc;

    @Column(name = "file_path", length = 255)
    private String filePath;

    @Column(name = "content_type", length = 100)
    private String contentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
}
