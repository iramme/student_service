package com.campus.studentservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // ✅ désactive CSRF (important pour APIs)
            .cors(cors -> {})             // ✅ active CORS
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/students/**").permitAll()
                .requestMatchers("/api/universities/**").permitAll()
                .anyRequest().permitAll() // ✅ autorise tout pour test
            );

        return http.build();
    }

    // ✅ Autorisation CORS globale (pour que Node.js puisse accéder)
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*") // Autorise toutes les origines (localhost:4000 inclus)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }
}
