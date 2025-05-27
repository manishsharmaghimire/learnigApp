package com.elearn.dto;

import com.elearn.entity.Category;
import com.elearn.entity.Video;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto {

    private String id;

    private String title;

    private String shortDesc;

    private String longDesc;

    private double price;

    private boolean live = false;

    private double discount;

    private Date createdDate;

    private String banner;

    private String bannerContentType;

    private List<Video> videos = new ArrayList<>();

    private List<Category> categoryList = new ArrayList<>();

}
