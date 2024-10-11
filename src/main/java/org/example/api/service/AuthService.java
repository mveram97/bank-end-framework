package org.example.api.service;

import org.example.api.data.entity.Customer;
import org.example.api.token.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private Token tokenService; // Inyectamos el servicio de token

    // Simulación de un método de autenticación
    public String login(String email, String password) {
        // Lógica para autenticar al cliente (ej. verificar credenciales)
        Customer customer = authenticate(email, password);

        if (customer != null) {
            // Generar un nuevo token si la autenticación es exitosa
            return tokenService.generateToken(customer.getEmail());
        } else {
            throw new RuntimeException("Invalid credentials"); // O lanza una excepción adecuada
        }
    }

    // Método simulado para la autenticación
    private Customer authenticate(String email, String password) {

        // Aquí deberías implementar la lógica para verificar las credenciales del cliente.
        // Por ejemplo, buscar en la base de datos si el email y la contraseña son correctos.

        // Simulación de búsqueda en base de datos
        // En un caso real, deberías recuperar el cliente de una base de datos

        if ("customer@example.com".equals(email) && "password123".equals(password)) {
            Customer customer = new Customer();
            customer.setEmail(email);
            customer.setName("Jane");
            customer.setSurname("Doe");
            return customer;
        }
        return null; // Retornar null si las credenciales son inválidas
    }

}

