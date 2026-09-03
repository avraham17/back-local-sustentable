package edu.co.sena.worksite.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // habilita @PreAuthorize en los controllers/services
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // Recursos estáticos del frontend: html, css, js, imágenes, fuentes, favicon
                        // (se incluyen variantes con mayúscula porque el proyecto mezcla "js/" y "Js/")
                        .requestMatchers(HttpMethod.GET,
                                "/", "/*.html", "/**/*.html",
                                "/css/**", "/Css/**",
                                "/js/**", "/Js/**",
                                "/img/**", "/Img/**",
                                "/images/**", "/Images/**",
                                "/assets/**", "/Assets/**",
                                "/fonts/**", "/Fonts/**",
                                "/favicon.ico", "/*.png", "/*.jpg", "/*.jpeg", "/*.gif", "/*.webp", "/*.svg", "/*.ico"
                        ).permitAll()

                        .requestMatchers(HttpMethod.POST, "/ResgistroUsuario").permitAll()
                        .requestMatchers(HttpMethod.POST, "/ResgistroUsuario/login").permitAll()


                        .requestMatchers(HttpMethod.GET, "/oferta/**").permitAll()

                        // Todo lo demás requiere un token válido
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager();
    }

    // ojo en desarrollo, "*" (cualquier origen). En  producción debemos, sobrescribir
    // en application.properties con el/los dominio(s) reales del frontend, ej:
    // worksite.cors.origenes=https://tuapp.com,https://www.tuapp.com


    @Value("${worksite.cors.origenes:*}")
    private String origenesPermitidos;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList(origenesPermitidos.split(",")));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}