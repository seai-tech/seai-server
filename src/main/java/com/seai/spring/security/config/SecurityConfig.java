package com.seai.spring.security.config;

import com.seai.spring.security.filter.TrainingCentersAuthFilter;
import com.seai.spring.security.filter.UsersAuthFilter;
import com.seai.spring.security.service.TrainingCenterDetailsServiceImpl;
import com.seai.spring.security.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UsersAuthFilter usersAuthFilter;

    private final TrainingCentersAuthFilter trainingCentersAuthFilter;

    private final UserDetailsServiceImpl userDetailsServiceImpl;

    private final TrainingCenterDetailsServiceImpl trainingCenterDetailsService;

    private final PasswordEncoder passwordEncoder;

    // Configuring HttpSecurity
    @Bean
    @Order(1)
    public SecurityFilterChain usersFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/api/v1/users/**", "/api/v1/ships/**").csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers(HttpMethod.POST, "/api/v1/users", "/api/v1/users/authentication")
                        .permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**")
                        .permitAll()
                        .requestMatchers("/api/v1/users/**", "/api/v1/ships/**")
                        .authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .authenticationProvider(usersAuthenticationProvider()).addFilterBefore(
                        usersAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    // Configuring HttpSecurity
    @Bean
    @Order(2)
    public SecurityFilterChain trainingCentersFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/api/v1/training-centers/**").csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers(HttpMethod.POST, "/api/v1/training-centers", "/api/v1/training-centers/authentication")
                        .permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**")
                        .permitAll()
                        .requestMatchers("/api/v1/training-centers/**")
                        .authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .authenticationProvider(trainingCentersAuthenticationProvider()).addFilterBefore(
                        trainingCentersAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*").allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE");
            }
        };
    }

    @Bean
    public AuthenticationProvider usersAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsServiceImpl);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    @Bean
    public AuthenticationProvider trainingCentersAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(trainingCenterDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(usersAuthenticationProvider());
        authenticationManagerBuilder.authenticationProvider(trainingCentersAuthenticationProvider());
        return authenticationManagerBuilder.build();
    }
}
