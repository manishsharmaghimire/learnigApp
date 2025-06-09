package com.elearn.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @Column(length = 50)
    private String id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(name = "description")
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "added_date")
    private Date addedDate;

    @ManyToMany(mappedBy = "categoryList", cascade = CascadeType.ALL)
    private List<Course> courses = new ArrayList<>();


    public void addCourse(Course course) {
        if (!courses.contains(course)) {
            courses.add(course);
            course.getCategoryList().add(this);
        }
    }

    public void removeCourse(Course course) {
        if (courses.contains(course)) {
            courses.remove(course);
            course.getCategoryList().remove(this);
        }
    }
}
