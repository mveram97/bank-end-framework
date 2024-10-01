Feature: Log in API

  Scenario Outline: User tries to log in
    When I send a "POST" to "https://reqres.in/api/login" with  "<email>" and "<password>"
    Then Result should be <code>

  Examples:
      | email    | password    | code                     |
      | eve.holt@reqres.in | cityslicka | 200 |
      | peter@klaven |  | 400 |
