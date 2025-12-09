package com.example.OnlyKick.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value; 
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.OnlyKick.security.JwtFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final UserDetailsService userDetailsService;

    // Inyectamos la URL del frontend desde application.properties
    @Value("${frontend.url}")
    private String frontendUrl;

    public SecurityConfig(JwtFilter jwtFilter, UserDetailsService userDetailsService) {
        this.jwtFilter = jwtFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Configuración de CORS usando nuestra fuente personalizada
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // --- RUTAS PÚBLICAS ---
                .requestMatchers("/api/v1/usuarios/login", "/api/v1/usuarios/registro").permitAll()
                .requestMatchers("/doc/**", "/v3/api-docs/**", "/swagger-ui/**").permitAll()

                .requestMatchers("/actuator/**").permitAll()
                
                // Permitir VER productos e imágenes a cualquier visitante (GET)
                .requestMatchers(HttpMethod.GET, "/api/v1/productos/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/imagenes/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/categorias/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/marcas/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/tallas/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/colores/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/generos/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/materiales/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/regiones/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/comunas/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/metodos-envio/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/metodos-pago/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/inventario/**").permitAll()

                // --- RUTAS PRIVADAS (Todo lo demás requiere Token) ---
                .anyRequest().authenticated()
            )
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Aquí usamos la variable dinámica. 
        // En local será "http://localhost:5173", en Render será tu dominio de Vercel.
        configuration.setAllowedOrigins(List.of(frontendUrl)); 
        
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true); 

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}