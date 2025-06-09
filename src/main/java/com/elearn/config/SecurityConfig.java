package com.elearn.config;

import com.elearn.exception.CustomAccessDeniedHandler;
import com.elearn.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthenticationEntryPoint entryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                            "/api/v1/auth/**",
                            "/error",
                            "/v3/api-docs/**",
                            "/swagger-ui/**",
                            "/swagger-ui.html"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(entryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                )
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }


/* <<<<<<<<<<<<<<  ✨ Windsurf Command ⭐ >>>>>>>>>>>>>>>> */
    /**
     * A bean that provides CORS configuration for all endpoints.
     *
     * <p>This configuration allows requests from any origin, with any of the
     * following methods: GET, POST, PUT, PATCH, DELETE, OPTIONS. It also allows any
     * of the following headers: authorization, content-type, x-auth-token. Finally,
     * it exposes the x-auth-token header to the client.
     *
     * @return a bean that provides CORS configuration for all endpoints.
     */
/* <<<<<<<<<<  104012c5-0df0-4674-b0ce-9144f8010ea0  >>>>>>>>>>> */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(new ArrayList<>(AppConstants.Cors.ALLOWED_ORIGINS));
        configuration.setAllowedMethods(new ArrayList<>(AppConstants.Cors.ALLOWED_METHODS));
        configuration.setAllowedHeaders(new ArrayList<>(AppConstants.Cors.ALLOWED_HEADERS));
        configuration.setExposedHeaders(new ArrayList<>(AppConstants.Cors.EXPOSED_HEADERS));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
