package com.elearn.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;
    /**
     * @param request
     * @param response
     * @param accessDeniedException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void handle(HttpServletRequest request,
                     HttpServletResponse response,
                     AccessDeniedException accessDeniedException)
            throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("timestamp", Instant.now().toString());
        responseData.put("status", HttpServletResponse.SC_FORBIDDEN);
        responseData.put("error", "Access Denied");
        responseData.put("message", accessDeniedException.getMessage() != null ? 
            accessDeniedException.getMessage() : "You don't have permission to access this resource");
        responseData.put("path", request.getRequestURI());

        try (

                PrintWriter writer = response.getWriter()) {
            writer.write(objectMapper.writeValueAsString(responseData));
            writer.flush();
        }
    }
}
