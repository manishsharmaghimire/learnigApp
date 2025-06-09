package com.elearn.validation;

/**
 * Validates international phone number format.
 */
public class PhoneNumberValidator extends BaseValidator<ValidPhoneNumber> {
    
    private static final String PHONE_PATTERN = "^\\+?[0-9\\s\\(\\)\\-]{10,20}$";
    
    @Override
    public void initialize(ValidPhoneNumber constraintAnnotation) {
        this.pattern = PHONE_PATTERN;
        this.message = constraintAnnotation.message();
    }
}
