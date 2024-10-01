Feature: Test API
  Scenario: Create a user
    When I send a "POST" to "https://reqres.in/api/users"
    Then Result should be 201

  Scenario: Update a user
    When I send a "PUT" to "https://reqres.in/api/users/2"
    Then Result should be 200