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
        String uri = request.getRequestURI();

        System.out.println(">>> [JwtAuthFilter] URI: " + uri);
        System.out.println(">>> [JwtAuthFilter] Authorization header: " + authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            boolean valido = jwtUtil.isValido(token);
            System.out.println(">>> [JwtAuthFilter] Token válido: " + valido);

            if (valido) {
                Claims claims = jwtUtil.extrairClaims(token);
                String email = claims.get("email", String.class);
                String role = claims.get("role", String.class);
                System.out.println(">>> [JwtAuthFilter] Email: " + email + " | Role: " + role);

                var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
                var auth = new UsernamePasswordAuthenticationToken(email, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } else {
            System.out.println(">>> [JwtAuthFilter] Nenhum token Bearer encontrado!");
        }

        filterChain.doFilter(request, response);
    }
}