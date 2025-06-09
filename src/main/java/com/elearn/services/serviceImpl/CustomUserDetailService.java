package com.elearn.services.serviceImpl;

import com.elearn.dto.CustomUserDetails;
import com.elearn.entity.User;
import com.elearn.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepo;
    /**
     * @param username 
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = this.userRepo.findByEmail(username)
                .orElseThrow(() -> new BadCredentialsException("User not found in database !!"));
        return new CustomUserDetails(user);
    }
}
