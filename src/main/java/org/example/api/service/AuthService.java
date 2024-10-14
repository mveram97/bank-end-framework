package org.example.api.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.example.api.data.entity.Customer;
import org.example.api.data.repository.CustomerRepository;
import org.example.api.data.request.LoginRequest;
import org.example.api.token.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.WebUtils;
import org.example.api.token.Token;
import java.util.Optional;

@Service
public class AuthService {


    @Autowired
    private Token token;

    private String jwtCookie = "cookieToken";

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    private Token tokenService; // Inyectamos el servicio de token


    public boolean authenticate(String email, String password) {
        Optional<Customer> customerOpt = customerRepository.findByEmail(email);

        if (customerOpt.isPresent()){
            Customer customer = customerOpt.get();
            return password.equals(customer.getPassword()) ;

        } else {
            return false;
        }
    }

    public ResponseCookie generateJwtCookie(LoginRequest loginRequest) {
        String jwt = tokenService.generateToken(loginRequest.getEmail());
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt).path("/api").maxAge(24 * 60 * 60).httpOnly(true).build();
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

    public Boolean AreYouLogged(HttpServletRequest request) {
        String jwt = getJwtFromCookies(request);

        if (jwt == null || !token.validateToken(jwt)) {
            return true;
        }
        return false;
    }
}

