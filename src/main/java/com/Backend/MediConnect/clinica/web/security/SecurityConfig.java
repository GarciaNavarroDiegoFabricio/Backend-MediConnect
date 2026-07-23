package com.Backend.MediConnect.clinica.web.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CorsConfigurationSource corsConfigurationSource;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            CorsConfigurationSource corsConfigurationSource,
            CustomAccessDeniedHandler accessDeniedHandler,
            CustomAuthenticationEntryPoint authenticationEntryPoint) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.corsConfigurationSource = corsConfigurationSource;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/"
                        ).permitAll()

                        .requestMatchers("/api/auth/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/sedes/detalle").hasRole("ADMINISTRADOR_TOTAL")
                        .requestMatchers(HttpMethod.GET, "/api/sedes/*/detalle").hasRole("ADMINISTRADOR_TOTAL")
                        .requestMatchers(HttpMethod.GET, "/api/sedes/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/especialidades/detalle").hasRole("ADMINISTRADOR_TOTAL")
                        .requestMatchers(HttpMethod.GET, "/api/especialidades/*/detalle").hasRole("ADMINISTRADOR_TOTAL")
                        .requestMatchers(HttpMethod.GET, "/api/especialidades/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/horarios-medico/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/medicos/disponibles").permitAll()

                        .requestMatchers("/api/reniec/**").authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/perfil").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/perfil").authenticated()

                        .requestMatchers(HttpMethod.PUT, "/api/foto-perfil/mi-foto").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/foto-perfil/mi-foto").authenticated()
                        .requestMatchers("/api/foto-perfil/**")
                        .hasAnyRole("ADMINISTRADOR_TOTAL", "ADMINISTRADOR_LOCAL", "RECEPCIONISTA")

                        .requestMatchers(HttpMethod.POST, "/api/usuarios").hasRole("ADMINISTRADOR_TOTAL")
                        .requestMatchers(HttpMethod.PUT, "/api/usuarios/*").hasRole("ADMINISTRADOR_TOTAL")
                        .requestMatchers(HttpMethod.GET, "/api/usuarios").hasAnyRole("ADMINISTRADOR_TOTAL", "ADMINISTRADOR_LOCAL")
                        .requestMatchers(HttpMethod.GET, "/api/usuarios/*").hasAnyRole("ADMINISTRADOR_TOTAL", "ADMINISTRADOR_LOCAL")
                        .requestMatchers(HttpMethod.PATCH, "/api/usuarios/*/bloquear").hasRole("ADMINISTRADOR_TOTAL")
                        .requestMatchers(HttpMethod.PATCH, "/api/usuarios/*/inactivar").hasRole("ADMINISTRADOR_TOTAL")
                        .requestMatchers(HttpMethod.DELETE, "/api/usuarios/*").hasRole("ADMINISTRADOR_TOTAL")

                        .requestMatchers(HttpMethod.POST, "/api/medicos/*/completar-datos").hasRole("ADMINISTRADOR_TOTAL")
                        .requestMatchers(HttpMethod.PATCH, "/api/medicos/*/especialidad-sede").hasRole("ADMINISTRADOR_TOTAL")
                        .requestMatchers(HttpMethod.GET, "/api/medicos/mi-perfil").hasRole("MEDICO")
                        .requestMatchers(HttpMethod.PATCH, "/api/medicos/*/disponibilidad")
                        .hasAnyRole("ADMINISTRADOR_TOTAL", "ADMINISTRADOR_LOCAL", "MEDICO")
                        .requestMatchers(HttpMethod.PATCH, "/api/medicos/*/inactivar")
                        .hasAnyRole("ADMINISTRADOR_TOTAL", "ADMINISTRADOR_LOCAL")
                        .requestMatchers(HttpMethod.PATCH, "/api/medicos/*/activar")
                        .hasAnyRole("ADMINISTRADOR_TOTAL", "ADMINISTRADOR_LOCAL")
                        .requestMatchers(HttpMethod.GET, "/api/medicos").hasAnyRole(
                                "ADMINISTRADOR_TOTAL",
                                "ADMINISTRADOR_LOCAL",
                                "RECEPCIONISTA"
                        )
                        .requestMatchers(HttpMethod.GET, "/api/medicos/*").hasAnyRole(
                                "ADMINISTRADOR_TOTAL",
                                "ADMINISTRADOR_LOCAL",
                                "RECEPCIONISTA"
                        )

                        .requestMatchers(HttpMethod.POST, "/api/pacientes/*/completar-datos")
                        .hasAnyRole("ADMINISTRADOR_TOTAL", "ADMINISTRADOR_LOCAL", "RECEPCIONISTA")
                        .requestMatchers(HttpMethod.PUT, "/api/pacientes/mi-contacto").hasRole("PACIENTE")
                        .requestMatchers(HttpMethod.GET, "/api/pacientes/mi-contacto").hasRole("PACIENTE")
                        .requestMatchers(HttpMethod.GET, "/api/pacientes/buscar")
                        .hasAnyRole("ADMINISTRADOR_TOTAL", "ADMINISTRADOR_LOCAL", "RECEPCIONISTA")

                        .requestMatchers(HttpMethod.POST, "/api/especialidades").hasRole("ADMINISTRADOR_TOTAL")
                        .requestMatchers(HttpMethod.PUT, "/api/especialidades/*").hasRole("ADMINISTRADOR_TOTAL")
                        .requestMatchers(HttpMethod.PUT, "/api/especialidades/*/foto").hasRole("ADMINISTRADOR_TOTAL")
                        .requestMatchers(HttpMethod.DELETE, "/api/especialidades/*/foto").hasRole("ADMINISTRADOR_TOTAL")
                        .requestMatchers(HttpMethod.DELETE, "/api/especialidades/*").hasRole("ADMINISTRADOR_TOTAL")

                        .requestMatchers(HttpMethod.POST, "/api/sedes").hasRole("ADMINISTRADOR_TOTAL")
                        .requestMatchers(HttpMethod.PUT, "/api/sedes/*").hasRole("ADMINISTRADOR_TOTAL")
                        .requestMatchers(HttpMethod.PUT, "/api/sedes/*/foto").hasRole("ADMINISTRADOR_TOTAL")
                        .requestMatchers(HttpMethod.DELETE, "/api/sedes/*/foto").hasRole("ADMINISTRADOR_TOTAL")
                        .requestMatchers(HttpMethod.PATCH, "/api/sedes/*/activar").hasRole("ADMINISTRADOR_TOTAL")
                        .requestMatchers(HttpMethod.PATCH, "/api/sedes/*/inactivar").hasRole("ADMINISTRADOR_TOTAL")
                        .requestMatchers(HttpMethod.DELETE, "/api/sedes/*").hasRole("ADMINISTRADOR_TOTAL")

                        .requestMatchers(HttpMethod.POST, "/api/horarios-medico/mi-horario").hasRole("MEDICO")
                        .requestMatchers(HttpMethod.GET, "/api/horarios-medico/mi-horario").hasRole("MEDICO")
                        .requestMatchers(HttpMethod.POST, "/api/horarios-medico/*")
                        .hasAnyRole("ADMINISTRADOR_TOTAL", "ADMINISTRADOR_LOCAL", "MEDICO")
                        .requestMatchers(HttpMethod.PUT, "/api/horarios-medico/*")
                        .hasAnyRole("ADMINISTRADOR_TOTAL", "ADMINISTRADOR_LOCAL", "MEDICO")
                        .requestMatchers(HttpMethod.PATCH, "/api/horarios-medico/*/inactivar")
                        .hasAnyRole("ADMINISTRADOR_TOTAL", "ADMINISTRADOR_LOCAL", "MEDICO")

                        .requestMatchers(HttpMethod.POST, "/api/bloqueos-horario/mi-bloqueo").hasRole("MEDICO")
                        .requestMatchers(HttpMethod.POST, "/api/bloqueos-horario/*")
                        .hasAnyRole("ADMINISTRADOR_TOTAL", "ADMINISTRADOR_LOCAL", "MEDICO")
                        .requestMatchers(HttpMethod.DELETE, "/api/bloqueos-horario/*")
                        .hasAnyRole("ADMINISTRADOR_TOTAL", "ADMINISTRADOR_LOCAL", "MEDICO")
                        .requestMatchers(HttpMethod.GET, "/api/bloqueos-horario/*")
                        .hasAnyRole("ADMINISTRADOR_TOTAL", "ADMINISTRADOR_LOCAL", "RECEPCIONISTA", "MEDICO")

                        .requestMatchers(HttpMethod.POST, "/api/citas").hasRole("PACIENTE")
                        .requestMatchers(HttpMethod.PATCH, "/api/citas/*/reprogramar")
                        .hasAnyRole("ADMINISTRADOR_TOTAL", "ADMINISTRADOR_LOCAL", "RECEPCIONISTA", "PACIENTE")
                        .requestMatchers(HttpMethod.PATCH, "/api/citas/*/cancelar")
                        .hasAnyRole("ADMINISTRADOR_TOTAL", "ADMINISTRADOR_LOCAL", "RECEPCIONISTA", "PACIENTE")
                        .requestMatchers(HttpMethod.PATCH, "/api/citas/*/atendida")
                        .hasAnyRole("ADMINISTRADOR_TOTAL", "ADMINISTRADOR_LOCAL", "MEDICO")
                        .requestMatchers(HttpMethod.PATCH, "/api/citas/*/no-asistio")
                        .hasAnyRole("ADMINISTRADOR_TOTAL", "ADMINISTRADOR_LOCAL", "MEDICO")
                        .requestMatchers(HttpMethod.GET, "/api/citas/mis-citas").hasRole("PACIENTE")
                        .requestMatchers(HttpMethod.GET, "/api/citas/medico/*")
                        .hasAnyRole("ADMINISTRADOR_TOTAL", "ADMINISTRADOR_LOCAL", "RECEPCIONISTA")
                        .requestMatchers(HttpMethod.GET, "/api/citas/estado/*")
                        .hasAnyRole("ADMINISTRADOR_TOTAL", "ADMINISTRADOR_LOCAL", "RECEPCIONISTA")
                        .requestMatchers(HttpMethod.GET, "/api/citas/*/historial")
                        .hasAnyRole("ADMINISTRADOR_TOTAL", "ADMINISTRADOR_LOCAL", "RECEPCIONISTA", "MEDICO", "PACIENTE")
                        .requestMatchers(HttpMethod.GET, "/api/citas/*")
                        .hasAnyRole("ADMINISTRADOR_TOTAL", "ADMINISTRADOR_LOCAL", "RECEPCIONISTA", "MEDICO", "PACIENTE")
                        .requestMatchers(HttpMethod.GET, "/api/citas")
                        .hasAnyRole("ADMINISTRADOR_TOTAL", "ADMINISTRADOR_LOCAL", "RECEPCIONISTA")

                        .requestMatchers(HttpMethod.POST, "/api/historias-clinicas/atenciones/*/iniciar").hasRole("MEDICO")
                        .requestMatchers(HttpMethod.POST, "/api/historias-clinicas/atenciones/*/signos-vitales").hasRole("MEDICO")
                        .requestMatchers(HttpMethod.POST, "/api/historias-clinicas/atenciones/*/diagnosticos").hasRole("MEDICO")
                        .requestMatchers(HttpMethod.POST, "/api/historias-clinicas/atenciones/*/tratamientos").hasRole("MEDICO")
                        .requestMatchers(HttpMethod.PATCH, "/api/historias-clinicas/atenciones/*/cerrar").hasRole("MEDICO")
                        .requestMatchers(HttpMethod.GET, "/api/historias-clinicas/atenciones/*/constancia")
                        .hasAnyRole("ADMINISTRADOR_TOTAL", "ADMINISTRADOR_LOCAL", "MEDICO", "PACIENTE")
                        .requestMatchers(HttpMethod.POST, "/api/historias-clinicas/pacientes/*/antecedentes")
                        .hasAnyRole("ADMINISTRADOR_TOTAL", "ADMINISTRADOR_LOCAL", "MEDICO", "RECEPCIONISTA")
                        .requestMatchers(HttpMethod.POST, "/api/historias-clinicas/pacientes/*/documentos")
                        .hasAnyRole("ADMINISTRADOR_TOTAL", "ADMINISTRADOR_LOCAL", "MEDICO", "RECEPCIONISTA")
                        .requestMatchers(HttpMethod.PUT, "/api/historias-clinicas/documentos/*")
                        .hasAnyRole("ADMINISTRADOR_TOTAL", "ADMINISTRADOR_LOCAL", "MEDICO", "RECEPCIONISTA")
                        .requestMatchers(HttpMethod.PUT, "/api/historias-clinicas/documentos/*/archivo")
                        .hasAnyRole("ADMINISTRADOR_TOTAL", "ADMINISTRADOR_LOCAL", "MEDICO", "RECEPCIONISTA")
                        .requestMatchers(HttpMethod.DELETE, "/api/historias-clinicas/documentos/*")
                        .hasAnyRole("ADMINISTRADOR_TOTAL", "ADMINISTRADOR_LOCAL", "MEDICO", "RECEPCIONISTA")
                        .requestMatchers(HttpMethod.GET, "/api/historias-clinicas/documentos/*")
                        .hasAnyRole("ADMINISTRADOR_TOTAL", "ADMINISTRADOR_LOCAL", "MEDICO", "RECEPCIONISTA", "PACIENTE")
                        .requestMatchers(HttpMethod.GET, "/api/historias-clinicas/pacientes/*/documentos")
                        .hasAnyRole("ADMINISTRADOR_TOTAL", "ADMINISTRADOR_LOCAL", "MEDICO", "RECEPCIONISTA", "PACIENTE")
                        .requestMatchers(HttpMethod.GET, "/api/historias-clinicas/atenciones/*/documentos")
                        .hasAnyRole("ADMINISTRADOR_TOTAL", "ADMINISTRADOR_LOCAL", "MEDICO", "RECEPCIONISTA")
                        .requestMatchers(HttpMethod.GET, "/api/historias-clinicas/pacientes/*")
                        .hasAnyRole("ADMINISTRADOR_TOTAL", "ADMINISTRADOR_LOCAL", "MEDICO", "PACIENTE")

                        .requestMatchers(HttpMethod.POST, "/api/recetas/atenciones/*").hasRole("MEDICO")
                        .requestMatchers(HttpMethod.GET, "/api/recetas/atenciones/*")
                        .hasAnyRole("ADMINISTRADOR_TOTAL", "ADMINISTRADOR_LOCAL", "MEDICO", "PACIENTE")
                        .requestMatchers(HttpMethod.GET, "/api/recetas/pacientes/*")
                        .hasAnyRole("ADMINISTRADOR_TOTAL", "ADMINISTRADOR_LOCAL", "MEDICO", "PACIENTE")
                        .requestMatchers(HttpMethod.GET, "/api/recetas/*/pdf")
                        .hasAnyRole("ADMINISTRADOR_TOTAL", "ADMINISTRADOR_LOCAL", "MEDICO", "PACIENTE")
                        .requestMatchers(HttpMethod.GET, "/api/recetas/*")
                        .hasAnyRole("ADMINISTRADOR_TOTAL", "ADMINISTRADOR_LOCAL", "MEDICO", "PACIENTE")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}