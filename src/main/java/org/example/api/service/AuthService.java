package org.example.api.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.example.api.data.entity.Customer;
import org.example.api.token.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;
import org.example.api.data.request.LoginRequest;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private Token tokenService; // Inyectamos el servicio de token

    private final String jwtCookie = "cookieToken";

    // Lógica para autenticar al cliente (ej. verificar credenciales)
    public boolean authenticate(String email, String password) {
        Optional<Customer> custE= customerService.findByEmail(email);
        Optional<Customer> custP = customerService.findByPassword(password);
        // Si se ha encontrado el usuario (con email y password validos) -> devolver true
        // Retornar falso si las credenciales son inválidas
        return custP.isPresent() && custE.isPresent() && custE.equals(custP);
    }

    public ResponseCookie generateJwtCookie(LoginRequest loginRequest) {
        String jwt = tokenService.generateToken(loginRequest.getEmail());
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt)
                .path("/public").maxAge(24 * 60 * 60).httpOnly(true).build();
        return cookie;
    }

    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }
}

