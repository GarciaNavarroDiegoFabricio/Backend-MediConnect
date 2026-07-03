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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

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
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/auth/me").hasAnyRole("ADMIN_LOCAL", "ADMIN_TOTAL" , "PACIENTE" ,"MEDICO")
                        .requestMatchers("/api/auth/registro/paciente").permitAll()
                        .requestMatchers("/api/auth/registro/admin-total").hasRole("ADMIN_TOTAL")
                        .requestMatchers("/api/auth/medicos").hasRole("ADMIN_LOCAL")
                        .requestMatchers("/api/auth/registro/admin-local").hasRole("ADMIN_TOTAL")
                        .requestMatchers("/api/auth/registro/medico").hasAnyRole("ADMIN_LOCAL", "ADMIN_TOTAL")
                        .requestMatchers("/api/paciente/**").hasRole("PACIENTE")
                        .requestMatchers("/api/medico/**").hasRole("MEDICO")
                        .requestMatchers("/api/admin-local/**").hasAnyRole("ADMIN_LOCAL", "ADMIN_TOTAL")
                        .requestMatchers("/api/admin-total/**").hasRole("ADMIN_TOTAL")
                        .requestMatchers("/api/sedes/**").permitAll()
                        .requestMatchers("/api/especialidades/**").permitAll()
                        .requestMatchers("/api/reniec/**").hasAnyRole("ADMIN_LOCAL", "ADMIN_TOTAL")
                        .requestMatchers("/api/pacientes/**")
                        .hasAnyRole("MEDICO", "ADMIN_LOCAL", "ADMIN_TOTAL")
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:5173",
                "http://localhost:3000",
                "https://backend-mediconnect-gdln.onrender.com",
                "https://frontend-medi-connect-54k6.vercel.app"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}