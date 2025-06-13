package com.example.crud.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.authentication.AuthenticationManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
        return new CustomSuccessHandler();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CustomUserDetailsService customUserDetailsService) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Allow all static resources without authentication
                .requestMatchers("/favicon.ico", "/static/**", "/images/**", "/css/**", "/js/**").permitAll()
                
                // Public endpoints
                .requestMatchers("/", "/login", "/register", "/auth/**").permitAll()
                
                // Motorcycle endpoints
                .requestMatchers(HttpMethod.GET, "/motorcycles", "/motorcycles/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/motorcycles/save", "/motorcycles/delete/**").hasRole("ADMIN")
                
                // Rental endpoints
                .requestMatchers(HttpMethod.GET, "/rentals", "/rentals/", "/rentals/admin", "/rentals/admin/**").hasRole("ADMIN")
                .requestMatchers("/rentals", "/rentals/create/**", "/rentals/return/**").hasRole("USER")
                .requestMatchers("/rentals/receipt/**").hasAnyRole("USER", "ADMIN")
                
                // Admin dashboard
                .requestMatchers("/admin/**").hasRole("ADMIN")
                
                // User dashboard
                .requestMatchers("/user/**").hasRole("USER")
                
                // New added
                .requestMatchers("/dashboard/**").hasRole("ADMIN")
                
                .anyRequest().authenticated()
            )
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/auth/login")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
                .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout", "GET"))
            )
            .formLogin(form -> form
                .loginPage("/auth/login")
                .loginProcessingUrl("/auth/login")
                .successHandler(myAuthenticationSuccessHandler())
                .permitAll()
            )
            .userDetailsService(customUserDetailsService);
        
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder, CustomUserDetailsService customUserDetailsService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
            .userDetailsService(customUserDetailsService)
            .passwordEncoder(passwordEncoder)
            .and()
            .build();
    }
}