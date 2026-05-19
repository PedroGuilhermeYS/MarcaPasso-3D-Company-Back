package br.edu.ifpe.MarcaPasso3D.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        System.err.println(">>> REQUEST: " + request.getMethod() + " " + request.getRequestURI());
        System.err.println(">>> AUTH HEADER: " + authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            System.err.println(">>> TOKEN INICIO: " + token.substring(0, Math.min(20, token.length())));

            boolean valido = jwtUtil.isValido(token);
            System.err.println(">>> TOKEN VALIDO: " + valido);

            if (valido) {
                Claims claims = jwtUtil.extrairClaims(token);
                String email = claims.get("email", String.class);
                String role = claims.get("role", String.class);
                System.err.println(">>> EMAIL: " + email + " | ROLE: " + role);
                System.err.println(">>> AUTHORITY: ROLE_" + role.toUpperCase());

                var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
                var auth = new UsernamePasswordAuthenticationToken(email, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);
                System.err.println(">>> AUTH SETADA NO CONTEXTO: " + auth.getAuthorities());
            }
        } else {
            System.err.println(">>> SEM BEARER TOKEN NA REQUISICAO");
        }

        filterChain.doFilter(request, response);
    }
}