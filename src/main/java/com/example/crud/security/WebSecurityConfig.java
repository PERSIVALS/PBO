package com.example.crud.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

     @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Allow all static resources without authentication
                .requestMatchers("/favicon.ico", "/static/**", "/images/**", "/css/**", "/js/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/motorcycles", "/motorcycles/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/motorcycles/save", "/motorcycles/delete/**").permitAll()
                .requestMatchers(
                    "/register", "/login", "/tailwind/**",
                    "/rentals", "/customers", "/dashboard"
                ).permitAll()
                .requestMatchers("/admin").permitAll()
                .requestMatchers("/user").permitAll()
                .anyRequest().authenticated()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID"));
        
        return http.build();
    }
}