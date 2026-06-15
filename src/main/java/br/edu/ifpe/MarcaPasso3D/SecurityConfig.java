package br.edu.ifpe.MarcaPasso3D;

import br.edu.ifpe.MarcaPasso3D.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        .requestMatchers("/actuator/health").permitAll()

                        .requestMatchers("/auth/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/produtos", "/produtos/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/cupons", "/api/cupons/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/cupons", "/api/cupons/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/cupons", "/api/cupons/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/cupons", "/api/cupons/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/cupons", "/api/cupons/**").hasRole("ADMIN")

                        // Cupons: leitura autenticada; escrita somente ADMIN
                        .requestMatchers(HttpMethod.GET,    "/api/cupons", "/api/cupons/**").authenticated()
                        .requestMatchers(HttpMethod.POST,   "/api/cupons", "/api/cupons/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/api/cupons", "/api/cupons/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH,  "/api/cupons", "/api/cupons/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/cupons", "/api/cupons/**").hasRole("ADMIN")

                        // Pedidos personalizados — rotas do usuário (autenticado)
                        .requestMatchers(HttpMethod.POST, "/api/personalizados/**").authenticated()
                        .requestMatchers(HttpMethod.GET,  "/api/personalizados/{idUsuario}").authenticated()
                        .requestMatchers(HttpMethod.GET,  "/api/personalizados/{idUsuario}/**").authenticated()

                        // Pedidos personalizados — rotas do admin
                        .requestMatchers(HttpMethod.GET,    "/api/personalizados").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,    "/api/personalizados/admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH,  "/api/personalizados/admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/personalizados/admin/**").hasRole("ADMIN")

                        // IA: qualquer um pode usar o chat (mesmo sem login)
                        .requestMatchers(HttpMethod.POST, "/chat-ia").permitAll()

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOriginPatterns(Arrays.asList(
                "http://localhost:*"
        ));
        config.setAllowedMethods(Arrays.asList(
                "GET",
                "POST",
                "PUT",
                "PATCH",
                "DELETE",
                "OPTIONS"
        ));
        config.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "Accept"
        ));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
