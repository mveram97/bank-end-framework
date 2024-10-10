package org.example;
import org.example.api.token.Token;

public class Main {
    public static void main(String[] args) {
        // Crear una instancia de Token
        Token tokenService = new Token();

        // Generar un JWT usando un email de cliente simulado
        String email = "customer@example.com";

        try {
            String jwt = tokenService.generateToken(email);
            System.out.println("Generated JWT: " + jwt);
            System.out.println(Token.getCustomerEmailFromJWT(jwt));
        } catch (Exception e) {
            System.err.println("Error generating token: " + e.getMessage());
        }
    }
}