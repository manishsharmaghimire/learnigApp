package com.elearn.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "courses")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Course {

    @Id
    @Column(length = 50)
    private String id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(name = "short_description", length = 300)
    private String shortDesc;

    @Lob
    @Column(name = "long_description", columnDefinition = "CLOB")
    private String longDesc;

    private double price;

    private boolean live = false;

    private double discount;


    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(length = 255)
    private String banner;

    @Column(name = "banner_content_type", length = 100)
    private String bannerContentType;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Video> videos = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "course_categories",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categoryList = new ArrayList<>();

    public void addCourse(Category category) {
        if (!categoryList.contains(category)) {
            categoryList.add(category);
            category.getCourses().add(this);
        }
    }

    public void removeCourse(Category category) {
        if (categoryList.contains(category)) {
            categoryList.remove(category);
            category.getCourses().remove(this);
        }
    }
}
