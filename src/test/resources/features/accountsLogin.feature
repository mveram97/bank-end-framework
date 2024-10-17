Feature: Accounts login endpoints.

  Background:
    Given the system is ready and i log with email "john.doe@example.com" and password "password123"

  Scenario: Logged account information
    When i request this users account information
    Then i should receive the code 200

  Scenario: Logged account amount
    When i request this users account amount
    Then i should receive the amount
