package org.example.api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.api.service.AuthService;
import org.example.api.token.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private AuthService authService;

    @Autowired
    private Token token;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String jwt = authService.getJwtFromCookies(request);

        if (jwt != null && token.validateToken(jwt)) {
            // Si el token es válido, permite la ejecución de la petición
            filterChain.doFilter(request, response);
        } else if (request.getRequestURI().startsWith("/public/")) {
            // Permitir las rutas públicas sin JWT
            filterChain.doFilter(request, response);
        } else {
            // Si el token no es válido o no está presente, responde con UNAUTHORIZED
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Cookie no válida o no presente");
        }
    }
}
