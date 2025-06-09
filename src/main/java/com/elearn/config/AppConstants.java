package com.elearn.config;

import java.io.File;
import java.util.List;

/**
 * Application-wide constants.
 * Note: Consider moving environment-specific values to application.properties/yml
 * and using @ConfigurationProperties or @Value for better maintainability.
 */
public final class AppConstants {
    
    private AppConstants() {
        // Private constructor to prevent instantiation
    }

    /**
     * Pagination defaults
     */
    public static final class Pagination {
        public static final String DEFAULT_PAGE_NUMBER = "0";
        public static final String DEFAULT_PAGE_SIZE = "10";
        public static final String DEFAULT_SORT_BY = "title";
        
        private Pagination() {}
    }
    
    /**
     * File system paths
     */
    public static final class Paths {
        public static final String UPLOADS = "uploads";
        public static final String COURSES = "courses";
        public static final String BANNERS = "banners";
        public static final String DEFAULT_PROFILE_PIC = "default.jpg";
        
        public static final String COURSE_BANNER_UPLOAD_DIR = 
            String.join(File.separator, UPLOADS, COURSES, BANNERS);
            
        public static final String DEFAULT_PROFILE_PIC_PATH = 
            String.join("/", UPLOADS, DEFAULT_PROFILE_PIC);
            
        private Paths() {}
    }
    
    /**
     * Application roles
     */
    public enum Role {
        ROLE_ADMIN,
        ROLE_GUEST;
        
/* <<<<<<<<<<<<<<  ✨ Windsurf Command 🌟 >>>>>>>>>>>>>>>> */
        /**
         * @return the authority of the role.
         */
        public String getAuthority() {
            return name();
        }

        /**
         * Returns the enum constant of this type with the specified name.
         * The string must match exactly an identifier used to declare an
         * enum constant in this type.
         *
         * @param name the name of the enum constant to be returned.
         * @return the enum constant with the specified name
         * @throws IllegalArgumentException if this enum type has no
         *         constant with the specified name
         * @throws NullPointerException if the argument is null
         */
        public static Role of(String name) {
            return Role.valueOf(name);
        }
/* <<<<<<<<<<  3e9a780c-7193-4765-998f-bd19d6cdae97  >>>>>>>>>>> */
    }
    
    /**
     * CORS configuration
     */
    public static final class Cors {
        public static final List<String> ALLOWED_ORIGINS = List.of(
            "http://localhost:3000",  // React default port
            "http://localhost:4200"   // Angular default port
        );

        public static final List<String> ALLOWED_METHODS = List.of(
            "GET", 
            "POST", 
            "PUT", 
            "PATCH", 
            "DELETE", 
            "OPTIONS"
        );

        public static final List<String> ALLOWED_HEADERS = List.of(
            "authorization", 
            "content-type", 
            "x-auth-token"
        );

        public static final List<String> EXPOSED_HEADERS = List.of(
            "x-auth-token"
        );
        
        private Cors() {}
    }
    
    /**
     * User-related messages
     */
    public static final class UserMessages {
        public static final String USER_NOT_FOUND = "User not found with id: %s";
        public static final String EMAIL_ALREADY_EXISTS = "User with email %s already exists";
        public static final String DEFAULT_ROLE_NOT_FOUND = "Default role not found";
        
        private UserMessages() {}
    }

    /**
     * Validation constants
     */
    public static final class Validation {
        public static final int TITLE_MAX_LENGTH = 100;
        public static final int SHORT_DESC_MAX_LENGTH = 300;
        public static final double MIN_PRICE = 0.0;
        public static final double MIN_DISCOUNT = 0.0;
        public static final int MAX_PAGE_SIZE = 100;
        public static final long MAX_FILE_SIZE_BYTES = 5 * 1024 * 1024; // 5MB
        public static final List<String> ALLOWED_FILE_TYPES = List.of("image/jpeg", "image/png", "image/gif");
        
        private Validation() {}
    }
}
