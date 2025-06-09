package com.elearn.services.serviceImpl;

import com.elearn.config.AppConstants;
import static com.elearn.config.AppConstants.UserMessages.*;
import com.elearn.dto.UserDto;
import com.elearn.dto.UserResponseDto;
import com.elearn.entity.Roles;
import com.elearn.entity.User;
import com.elearn.exception.DuplicateResourceException;
import com.elearn.exception.ResourceNotFoundException;
import com.elearn.repository.RoleRepo;
import com.elearn.repository.UserRepository;
import com.elearn.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service implementation for user management operations.
 *
 *
 * <p>This class provides the implementation of the {@link UserService} interface,
 * handling all business logic related to user management including creation, retrieval,
 * and validation of users. It leverages Spring's dependency injection for required
 * repositories and utilities.</p>
 * 
 * <p>This service is transactional, ensuring data consistency for all operations.
 * Any runtime exceptions will trigger a rollback of the current transaction.</p>
 * 
 * @see UserService
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {



    /** Repository for user data access operations. */
    private final UserRepository userRepository;
    
    /** Repository for role data access operations. */
    private final RoleRepo roleRepository;
    
    /** Service for encoding and validating passwords. */
    private final PasswordEncoder passwordEncoder;
    
    /** Mapper for converting between DTOs and entity objects. */
    private final ModelMapper modelMapper;

    /**
     * Creates a new user with the provided details.
     * 
     * @param userDto the user data transfer object containing user details
     * @return UserResponseDto containing the created user's information
     * @throws DuplicateResourceException if a user with the email already exists
     */
    /**
     * Creates a new user with the provided details.
     * 
     * <p>This method performs the following steps:
     * <ol>
     *   <li>Validates that the email is not already in use</li>
     *   <li>Creates a new user entity from the DTO</li>
     *   <li>Assigns a default role to the user</li>
     *   <li>Saves the user to the database</li>
     * </ol>
     * </p>
     *
     * @param userDto the user data transfer object containing user details
     * @return UserResponseDto containing the created user's information
     * @throws DuplicateResourceException if a user with the given email already exists
     * @throws IllegalStateException if the default role cannot be found
     */
    @Override
    public UserResponseDto create(@Valid UserDto userDto) {
        log.info("Creating new user with email: {}", userDto.getEmail());
        
        validateEmailNotInUse(userDto.getEmail());
        User newUser = createNewUser(userDto);
        User savedUser = saveUserWithDefaultRole(newUser);
        
        log.info("User created successfully with id: {}", savedUser.getUserId());
        return mapToUserResponseDto(savedUser);
    }

    /**
     * Retrieves a user by their unique identifier.
     *
     * <p>This method performs a read-only database lookup to find the user
     * with the specified ID. If no user is found, a {@link ResourceNotFoundException}
     * is thrown.</p>
     *
     * @param userId the unique identifier of the user to retrieve
     * @return UserDto containing the user's information
     * @throws ResourceNotFoundException if no user is found with the given ID
     */
    @Override
    @Transactional(readOnly = true)
    public UserDto getById(String userId) {
        log.debug("Fetching user with id: {}", userId);
        return userRepository.findById(userId)
                .map(this::mapToUserDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format(USER_NOT_FOUND, userId)
                ));
    }

    // ========== PRIVATE HELPER METHODS ==========
    
    /**
     * Validates that the given email is not already in use by another user.
     * 
     * <p>This method normalizes the email by converting it to lowercase and
     * trimming whitespace before checking for existence in the database.</p>
     *
     * @param email the email address to validate
     * @throws DuplicateResourceException if the email is already in use
     */
    private void validateEmailNotInUse(String email) {
        String normalizedEmail = email.toLowerCase().trim();
        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new DuplicateResourceException(
                String.format(EMAIL_ALREADY_EXISTS, email)
            );
        }
    }

    /**
     * Creates a new User entity from the provided DTO.
     * 
     * <p>This method performs the following operations:
     * <ul>
     *   <li>Maps DTO fields to the User entity</li>
     *   <li>Generates a new UUID for the user</li>
     *   <li>Hashes the user's password</li>
     *   <li>Sets default values for new users</li>
     *   <li>Normalizes email and phone number</li>
     * </ul>
     * </p>
     *
     * @param userDto the user data transfer object
     * @return a new User entity with the provided details
     */
    private User createNewUser(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        
        user.setUserId(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setCreatedAt(new Date());
        user.setProfilePath(AppConstants.Paths.DEFAULT_PROFILE_PIC_PATH);
        user.setActive(true);
        user.setEmailVarified(false);
        user.setSmsVerified(false);
        user.setEmail(user.getEmail().toLowerCase().trim());
        
        if (user.getPhoneNumber() != null) {
            user.setPhoneNumber(user.getPhoneNumber().trim());
        }
        
        return user;
    }

    /**
     * Saves a new user with the default role assigned.
     * 
     * <p>This method retrieves the default role (ROLE_GUEST) and assigns it
     * to the user before saving. If the default role cannot be found,
     * an {@link IllegalStateException} is thrown.</p>
     *
     * @param user the user to save
     * @return the saved user with default role assigned
     * @throws IllegalStateException if the default role cannot be found
     */
    private User saveUserWithDefaultRole(User user) {
        String defaultRoleName = AppConstants.Role.ROLE_GUEST.name();
        Roles defaultRole = roleRepository.findByRoleName(defaultRoleName)
                .orElseThrow(() -> new IllegalStateException(DEFAULT_ROLE_NOT_FOUND));
        
        user.assignRole(defaultRole);
        return userRepository.save(user);
    }

    /**
     * Maps a User entity to a UserResponseDto.
     * 
     * <p>This method converts the User entity to a response DTO, including
     * mapping the user's roles to role names. The password is never included
     * in the response DTO for security reasons.</p>
     *
     * @param user the user entity to map
     * @return the mapped UserResponseDto
     */
    private UserResponseDto mapToUserResponseDto(User user) {
        UserResponseDto responseDto = modelMapper.map(user, UserResponseDto.class);
        if (user.getRoles() != null) {
            responseDto.setRoles(mapRolesToRoleNames(user.getRoles()));
        }
        return responseDto;
    }

    /**
     * Maps a User entity to a UserDto.
     * 
     * <p>Note that the password is explicitly set to null in the DTO
     * to prevent accidental exposure of sensitive information.</p>
     *
     * @param user the user entity to map
     * @return the mapped UserDto with password set to null
     */
    private UserDto mapToUserDto(User user) {
        UserDto dto = modelMapper.map(user, UserDto.class);
        dto.setPassword(null); // Never expose password in DTO
        return dto;
    }

    /**
     * Converts a set of Roles to a set of role names.
     * 
     * @param roles the set of Roles to convert
     * @return an unmodifiable set of role names
     */
    private Set<String> mapRolesToRoleNames(Set<Roles> roles) {
        return roles.stream()
                .map(Roles::getRoleName)
                .collect(Collectors.toUnmodifiableSet());
    }
}
