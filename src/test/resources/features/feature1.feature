Feature: Test API
  Scenario: Create an user
    When I send a "POST" to "https://reqres.in/api/users"
    Then Result should be 201
