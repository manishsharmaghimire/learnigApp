package com.elearn.controller;

import com.elearn.dto.LoginRequest;
import com.elearn.dto.LoginResponse;
import com.elearn.dto.UserDto;
import com.elearn.dto.UserResponseDto;
import com.elearn.security.JwtUtil;
import com.elearn.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.AuthenticationException;


@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication and registration endpoints")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Operation(
            summary = "Authenticate user",
            description = "Authenticate user with email and password and return JWT token",
            tags = { "Authentication" }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated successfully",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping(
            value = "/login",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Step 1: Authenticate
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    );

            authenticationManager.authenticate(authenticationToken);

            // Step 2: Load user details
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());

            // Step 3: Generate token
            String token = jwtUtil.generateToken(userDetails.getUsername());

            // Step 4: Return custom response
            LoginResponse response = new LoginResponse(
                    token,
                    "Login successful",
                    userDetails.getUsername()
            );

            return ResponseEntity.ok(response);

        } catch (AuthenticationException ex) {//
            throw new BadCredentialsException("Invalid User Details!!");
        }
    }
    @Operation(
            summary = "Register a new user",
            description = "Create a new user account with the provided details",
            tags = { "Authentication" }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully",
                    content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping(
            value = "/register",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserDto userDto) {
        UserResponseDto createdUser = userService.create(userDto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }


}
