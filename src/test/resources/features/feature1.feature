Feature: Autenticación de Usuario y Recuperación de Cuentas

  Scenario: Inicio de sesión exitoso y obtención de cuentas del usuario
    When El usuario inicia sesión con email "john.doe@example.com" y contraseña "password123"
    Then Devuelve un codigo 200