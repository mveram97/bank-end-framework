Feature: LoginTest with GenericSteps and AuthenticationTestSteps

  Scenario Outline: I login with a user to test GenericSteps
    When I login with my email <email> and my password <password>
    Then I get a <status> status response and message: <message>

    Examples:
    |email                      |password             |status   |message                        |
    |"john.doe@example.com"     |"password123"         | 200     | "Correct authentication"      |
    |"john.doe@example.com"     |"password13"         | 400     | "Invalid credentials"         |
