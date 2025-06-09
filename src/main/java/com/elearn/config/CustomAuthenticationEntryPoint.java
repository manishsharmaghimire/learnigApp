package com.elearn.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    /**
     * @param request
     * @param response
     * @param authException
     * @throws IOException
     * @throws ServletException
     */
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("timestamp", Instant.now().toString());
        responseData.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        responseData.put("error", "Unauthorized");
        responseData.put("message", authException.getMessage() != null ? 
            authException.getMessage() : "Authentication failed");
        responseData.put("path", request.getRequestURI());

        try (PrintWriter writer = response.getWriter()) {
            writer.write(new ObjectMapper().writeValueAsString(responseData));
            writer.flush();
        }
    }
}

