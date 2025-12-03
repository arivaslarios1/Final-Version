package com.example.InvProject.security;

import com.example.InvProject.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getServletPath();

        // ✅ Do NOT apply JWT checking to:
        // - auth endpoints (register/login)
        // - h2-console
        // - static files
        if (path.startsWith("/api/auth")
                || path.startsWith("/h2-console")
                || path.startsWith("/assets")
                || path.startsWith("/logos")
                || path.equals("/")
                || path.endsWith(".html")
                || path.endsWith(".css")
                || path.endsWith(".js")
                || path.endsWith(".png")
                || path.endsWith(".jpg")
                || path.endsWith(".jpeg")
                || path.endsWith(".svg")) {

            filterChain.doFilter(request, response);
            return;
        }

        // ✅ For other endpoints: *try* to read JWT, but DO NOT 403 on failure.
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // no token → continue as anonymous; SecurityConfig will decide
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            Jws<Claims> jws = JwtService.parse(token);
            Claims claims = jws.getBody();

            String email = claims.getSubject(); // we used email as subject
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                Collections.emptyList() // no specific roles needed for this demo
                        );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        } catch (JwtException ex) {
            // Invalid or expired token → just continue WITHOUT authentication
            // (do NOT send 403; secured endpoints will return 401 automatically if needed)
        }

        filterChain.doFilter(request, response);
    }
}