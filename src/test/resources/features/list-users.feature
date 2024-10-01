Feature: Test API
  Scenario: Get list of users
    When I send a "GET" to "https://reqres.in/api/users?page=2"
    Then Result should be 200