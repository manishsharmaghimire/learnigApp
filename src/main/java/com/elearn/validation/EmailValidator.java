package com.elearn.validation;

/**
 * Validates email format using a standard email pattern.
 */
public class EmailValidator extends BaseValidator<ValidEmail> {
    
    private static final String EMAIL_PATTERN = 
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    
    @Override
    public void initialize(ValidEmail constraintAnnotation) {
        this.pattern = EMAIL_PATTERN;
        this.message = constraintAnnotation.message();
    }
}
