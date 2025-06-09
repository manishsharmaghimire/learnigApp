package com.elearn.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * Base class for all validators that use regex patterns.
 * @param <A> The constraint annotation type
 */
public abstract class BaseValidator<A extends java.lang.annotation.Annotation> 
    implements ConstraintValidator<A, String> {
    
    protected String pattern;
    protected String message;
    
    @Override
    public void initialize(A constraintAnnotation) {
        // To be implemented by subclasses
    }
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Let @NotBlank handle null values
        }
        return Pattern.matches(pattern, value);
    }
}
