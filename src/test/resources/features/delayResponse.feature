Feature: DelayResponse Feature API

  Scenario: Check all users with delay
    When I send a "GET" to "https://reqres.in/api/users?delay=3"
    And I have to wait 3 seconds
    Then Result should be 200