Feature: Test API
  Scenario: Create a user
    When I send a "POST" to "https://reqres.in/api/users"
    Then Result should be 201

  Scenario: Update a user
    Given A user with ID 2 exists
    When I send a "PUT" to "https://reqres.in/api/users/2"
    Then Result should be 200

  Scenario: Delete a user
    When I send a "DELETE" to "https://reqres.in/api/users/2"
    Then Result should be 204

  Scenario: See a user
    When I send a "GET" to "https://reqres.in/api/users/2"
    Then Result should be 200
