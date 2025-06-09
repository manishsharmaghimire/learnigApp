package com.elearn.validation;

import com.elearn.dto.CourseDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PriceAndDiscountValidator 
    implements ConstraintValidator<ValidPriceAndDiscount, CourseDto> {

    @Override
    public void initialize(ValidPriceAndDiscount constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(CourseDto courseDto, ConstraintValidatorContext context) {
        if (courseDto == null) {
            return true; // Let @NotNull handle this
        }
        
        if (courseDto.getDiscount() == null || courseDto.getPrice() == null) {
            return true; // Let @NotNull handle these
        }
        
        return courseDto.getDiscount() <= courseDto.getPrice();
    }
}
