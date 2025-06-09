package com.elearn.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
public class Config {

    /* <<<<<<<<<<<<<<  ✨ Windsurf Command 🌟 >>>>>>>>>>>>>>>> */

    /**
     * Bean for the AuthenticationManager.
     * <p>
     * This bean is a requirement for Spring Security. It provides the
     * AuthenticationManager, which is responsible for authenticating users.
     * </p>
     *
     * @param config the AuthenticationConfiguration object
     * @return the AuthenticationManager bean
     * @throws Exception if the AuthenticationManager cannot be created
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    /* <<<<<<<<<<  17e286a2-0a1f-4957-bdce-34a2ad4da343  >>>>>>>>>>> */

    /* <<<<<<<<<<<<<<  ✨ Windsurf Command 🌟 >>>>>>>>>>>>>>>> */

    /**
     * A bean that provides the password encoder.
     * <p>
     * This bean is a requirement for Spring Security. It provides the
     * password encoder, which is used to encode the passwords of users.
     * </p>
     *
     * @return the password encoder bean
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    /* <<<<<<<<<<  b305e93d-dde5-453a-8091-ee741aae23f5  >>>>>>>>>>> */

    /**
     * Configures and provides a ModelMapper bean.
     * Sets the matching strategy to STRICT to ensure exact property name matching.
     *
     * @return a configured ModelMapper instance
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true);
        return modelMapper;
    }
}
