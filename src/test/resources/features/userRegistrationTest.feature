Feature: Registration, login and logout of a new user

  Scenario: A customer register and login with valid credentials and logout
    Given I register with name "Jack", surname "Russell", email "jackrussell@gmail.com" and password "norton123" and I log in
    When I log out
    Then I should receive a message "Logged out successfully. Cookies cleared."

  Scenario: A costumer register and login with invalid credentials
    Given I have registered with name "Laura", surname "Mateo", email "lauramateo@gmail.com" and password "password123"
    When I have logged in with email "laurita@gmail.com" and password "password123"
    Then I should receive a message "Invalid credentials"