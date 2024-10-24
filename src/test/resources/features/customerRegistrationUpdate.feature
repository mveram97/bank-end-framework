Feature: Customer Registration and Update

  Scenario: A customer registers with a new account, updates their name and credentials, and verifies the changes

    Given The customer registers with random email, name, surname and password
    And The customer logging with the register credentials
    When The customer updates their name to "Rafa" and surname "Laguna"
    And The customer updates their email to "rafa.laguna6@example.com" and password to "newPassword123"
    Then The customer’s name, surname, email and password have been updated "successfully"