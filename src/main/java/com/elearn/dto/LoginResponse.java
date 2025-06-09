package com.elearn.dto;

import lombok.Data;

@Data
    public class LoginResponse {
        private String token;
        private String message;
        private String email;

        // Constructor
        public LoginResponse(String token, String message, String email) {
            this.token = token;
            this.message = message;
            this.email = email;
        }

        // Getters
        public String getToken() {
            return token;
        }

        public String getMessage() {
            return message;
        }

        public String getEmail() {
            return email;
        }
    }


