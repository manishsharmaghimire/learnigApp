package com.elearn.validation;

/**
 * Validates password strength requirements.
 */
public class PasswordValidator extends BaseValidator<ValidPassword> {
    
    private static final String PASSWORD_PATTERN = 
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,}$";
    
    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        this.pattern = PASSWORD_PATTERN;
        this.message = constraintAnnotation.message();
    }
}
