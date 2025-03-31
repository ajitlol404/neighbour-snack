package com.neighbour_snack.security;

import static com.neighbour_snack.constant.AppConstant.SIGN_UP_BASE_URL;

import java.time.Duration;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;

import com.neighbour_snack.dao.UserRepository;
import com.neighbour_snack.entity.User;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

        // Static resources excluded from authentication
        private static final String[] STATIC_RESOURCES = { "/font/**", "/css/**", "/dataTables/**", "/images/**",
                        "/javascript/**", "/favicon.ico", "/webjars/**" };

        // Public API endpoints
        private static final String[] PUBLIC_API_ENDPOINTS = {
                        "/", SIGN_UP_BASE_URL, "/categories", "/products", "/products/*/images"
        };

        private final UserRepository userRepository;
        private final LoginSuccessHandler loginSuccessHandler;

        @Bean
        PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        SecurityFilterChain configure(HttpSecurity http, AuthenticationProvider authenticationProvider)
                        throws Exception {
                http
                                .authorizeHttpRequests(authorizedHttpRequest -> authorizedHttpRequest
                                                .requestMatchers(STATIC_RESOURCES).permitAll()
                                                .requestMatchers(PUBLIC_API_ENDPOINTS).permitAll()
                                                .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                                                .anyRequest()
                                                .authenticated())
                                .authenticationProvider(authenticationProvider)
                                .formLogin(login -> login
                                                .loginPage("/login")
                                                .loginProcessingUrl("/login-process")
                                                .failureUrl("/login-fail")
                                                .permitAll()
                                                .successHandler(loginSuccessHandler))
                                .logout(logout -> logout
                                                .clearAuthentication(true)
                                                .invalidateHttpSession(true)
                                                .deleteCookies("JSESSIONID")
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/logout-success")
                                                .permitAll())
                                .headers(headerConfig -> headerConfig
                                                .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                                                .httpStrictTransportSecurity(hstsConfig -> hstsConfig
                                                                .includeSubDomains(true)
                                                                .maxAgeInSeconds(Duration.ofDays(365).toSeconds()))
                                                .xssProtection(xssProtectionConfig -> xssProtectionConfig
                                                                .headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
                                                .contentSecurityPolicy(contentSecurityPolicy -> contentSecurityPolicy
                                                                .policyDirectives(
                                                                                "default-src 'self'; style-src 'self' 'sha256-hMzqs20LuQL1AJI7RBQ1EGzMJRKMCUEeEbrhKZ8Z2Vg=' 'sha256-n1Ka7tYKSSn6Q706uoq7IDKKvBbp6Ni+ygmpVZgBcdg=' 'sha256-47DEQpj8HBSa+/TImW+5JCeuQeRkm5NMpJWZG3hSuFU=' 'sha256-3ITP0qhJJYBulKb1omgiT3qOK6k0iB3rMDhGfpM8b7c='; img-src 'self' data:; font-src 'self';")))
                                .sessionManagement(sessionManagement -> sessionManagement
                                                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

                return http.build();
        }

        @Bean
        AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
                DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
                daoAuthenticationProvider.setUserDetailsService(email -> {
                        User user = userRepository.findByEmail(email)
                                        .orElseThrow(() -> new UsernameNotFoundException("User not found."));
                        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                                        user.getPassword(),
                                        user.isEnabled(),
                                        true,
                                        true,
                                        true,
                                        List.of(new SimpleGrantedAuthority(user.getRole().name()))) {
                                public String getName() {
                                        return user.getName(); // Custom method to get name
                                }
                        };
                });
                daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
                return daoAuthenticationProvider;
        }

}
