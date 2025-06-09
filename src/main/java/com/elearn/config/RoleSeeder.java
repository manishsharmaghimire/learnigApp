package com.elearn.config;

import com.elearn.entity.Roles;
import com.elearn.exception.DataInitializationException;
import com.elearn.repository.RoleRepo;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Component responsible for initializing default roles in the system.
 * Runs automatically on application startup.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RoleSeeder {

    private final RoleRepo roleRepo;
    
    // List of default roles to be created
    private static final List<String> DEFAULT_ROLES = Arrays.asList(
        AppConstants.Role.ROLE_ADMIN.name(),
        AppConstants.Role.ROLE_GUEST.name()
    );

    /**
     * Initializes default roles on application startup.
     * Wrapped in try-catch to prevent application startup failure.
     */
    @PostConstruct
    @Transactional
    public void initRoles() {
        try {
            log.info("Starting role initialization...");
            
            DEFAULT_ROLES.forEach(this::createRoleIfNotExists);
            
            log.info("Role initialization completed successfully");
        } catch (DataAccessException e) {
            String errorMsg = "Failed to initialize roles: Database error";
            log.error(errorMsg, e);
            throw new DataInitializationException(errorMsg, e);
        } catch (Exception e) {
            String errorMsg = "Unexpected error during role initialization";
            log.error(errorMsg, e);
            throw new DataInitializationException(errorMsg, e);
        }
    }

    /**
     * Creates a new role if it doesn't already exist.
     *
     * @param roleName the name of the role to create
     * @throws DataInitializationException if role creation fails
     */
    private void createRoleIfNotExists(String roleName) {
        try {
            roleRepo.findByRoleName(roleName).orElseGet(() -> {
                log.debug("Creating new role: {}", roleName);
                
                Roles newRole = new Roles();
                newRole.setRoleId(UUID.randomUUID().toString());
                newRole.setRoleName(roleName);
                
                try {
                    Roles savedRole = roleRepo.save(newRole);
                    log.info("Created new role: {}", roleName);
                    return savedRole;
                } catch (Exception e) {
                    String errorMsg = String.format("Failed to create role: %s", roleName);
                    log.error(errorMsg, e);
                    throw new DataInitializationException(errorMsg, e);
                }
            });
        } catch (DataAccessException e) {
            String errorMsg = String.format("Database error while checking/creating role: %s", roleName);
            log.error(errorMsg, e);
            throw new DataInitializationException(errorMsg, e);
        }
    }
}
