package com.elearn.util;

import com.elearn.config.AppConstants;
import com.elearn.exception.InvalidInputException;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Utility class for common validation operations.
 */
public final class ValidationUtil {

    private ValidationUtil() {
        // Prevent instantiation
    }

    /**
     * Validates that an object is not null.
     * @param obj the object to check
     * @param fieldName the name of the field for error message
     * @throws InvalidInputException if the object is null
     */
    public static void validateNotNull(Object obj, String fieldName) {
        if (obj == null) {
            throw new InvalidInputException(fieldName + " cannot be null");
        }
    }

    /**
     * Validates that a string is not blank.
     * @param value the string to check
     * @param fieldName the name of the field for error message
     * @throws InvalidInputException if the string is null or blank
     */
    public static void validateNotBlank(String value, String fieldName) {
        if (!StringUtils.hasText(value)) {
            throw new InvalidInputException(fieldName + " is required");
        }
    }

    /**
     * Validates that a string does not exceed maximum length.
     * @param value the string to check
     * @param maxLength the maximum allowed length
     * @param fieldName the name of the field for error message
     * @throws InvalidInputException if the string exceeds maximum length
     */
    public static void validateMaxLength(String value, int maxLength, String fieldName) {
        if (value != null && value.length() > maxLength) {
            throw new InvalidInputException(fieldName + " cannot exceed " + maxLength + " characters");
        }
    }

    /**
     * Validates that a number is not negative.
     * @param value the number to check
     * @param fieldName the name of the field for error message
     * @throws InvalidInputException if the number is negative
     */
    public static void validateNotNegative(double value, String fieldName) {
        if (value < 0) {
            throw new InvalidInputException(fieldName + " cannot be negative");
        }
    }

    /**
     * Validates that discount is not greater than price.
     * @param price the price value
     * @param discount the discount value
     * @throws InvalidInputException if discount is greater than price
     */
    public static void validateDiscountNotGreaterThanPrice(double price, double discount) {
        if (discount > price) {
            throw new InvalidInputException("Discount cannot be greater than the price");
        }
    }

    /**
     * Validates a file upload.
     * @param file the file to validate
     * @throws InvalidInputException if the file is invalid
     */
    public static void validateFile(MultipartFile file) {
        validateNotNull(file, "File");
        
        if (file.isEmpty()) {
            throw new InvalidInputException("File cannot be empty");
        }
        
        if (file.getSize() > AppConstants.Validation.MAX_FILE_SIZE_BYTES) {
            throw new InvalidInputException("File size cannot exceed 5MB");
        }
        
        String contentType = file.getContentType();
        if (contentType == null || !AppConstants.Validation.ALLOWED_FILE_TYPES.contains(contentType)) {
            throw new InvalidInputException("Invalid file type. Allowed types: " + 
                String.join(", ", AppConstants.Validation.ALLOWED_FILE_TYPES));
        }
    }

    /**
     * Validates pagination parameters.
     * @param pageNumber the page number (0-based)
     * @param pageSize the number of items per page
     * @throws InvalidInputException if parameters are invalid
     */
    public static void validatePagination(int pageNumber, int pageSize) {
        if (pageNumber < 0) {
            throw new InvalidInputException("Page number cannot be negative");
        }
        
        if (pageSize <= 0 || pageSize > AppConstants.Validation.MAX_PAGE_SIZE) {
            throw new InvalidInputException("Page size must be between 1 and " + 
                AppConstants.Validation.MAX_PAGE_SIZE);
        }
    }
}
