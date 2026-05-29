package com.Backend.MediConnect.clinica.web.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/auth/registro/paciente").permitAll()
                        .requestMatchers("/api/auth/registro/admin-total").hasRole("ADMIN_TOTAL")
                        .requestMatchers("/api/auth/registro/admin-local").hasRole("ADMIN_TOTAL")
                        .requestMatchers("/api/auth/registro/medico").hasAnyRole("ADMIN_LOCAL", "ADMIN_TOTAL")
                        .requestMatchers("/api/paciente/**").hasRole("PACIENTE")
                        .requestMatchers("/api/medico/**").hasRole("MEDICO")
                        .requestMatchers("/api/admin-local/**").hasAnyRole("ADMIN_LOCAL", "ADMIN_TOTAL")
                        .requestMatchers("/api/admin-total/**").hasRole("ADMIN_TOTAL")
                        .requestMatchers("/api/sedes/**").authenticated()
                        .requestMatchers("/api/especialidades/**").authenticated()
                        .requestMatchers("/api/reniec/**").hasAnyRole("ADMIN_LOCAL", "ADMIN_TOTAL")
                        .anyRequest().authenticated())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}