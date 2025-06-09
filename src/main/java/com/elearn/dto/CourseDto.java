package com.elearn.dto;

import com.elearn.config.AppConstants;
import com.elearn.entity.Category;
import com.elearn.entity.Video;
import com.elearn.validation.ValidPriceAndDiscount;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ValidPriceAndDiscount(message = "Discount cannot be greater than price")
public class CourseDto {

    private String id;

    @NotBlank(message = "Title is required")
    @Size(max = AppConstants.Validation.TITLE_MAX_LENGTH, 
          message = "Title cannot exceed {max} characters")
    private String title;

    @Size(max = AppConstants.Validation.SHORT_DESC_MAX_LENGTH, 
          message = "Short description cannot exceed {max} characters")
    private String shortDesc;

    private String longDesc;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", message = "Price must be a positive number")
    private Double price;

    private boolean live = false;

    @DecimalMin(value = "0.0", message = "Discount cannot be negative")
    private Double discount = 0.0;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")

    private Date createdAt;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Date updatedAt;

    private String banner;
    private String bannerContentType;
    
    private List<Video> videos = new ArrayList<>();
    private List<Category> categoryList = new ArrayList<>();
}
