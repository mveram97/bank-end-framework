Feature: New user creates an account and make a transfer

  Scenario: The customer register, login, creates an account and makes a transfer to an existing user
    Given I register with name "Jack", surname "Russell", email "jackrussell@gmail.com" and password "norton123" and I log in
    When The customer creates 1 account with 5500.0 euros each
    And The customer make a transfer with their main account and transferAmount 300.0 to an account with id 449
    Then I should receive a message "Transfer made successfully"

  Scenario: The customer register, login, creates an account and makes a transfer to a non existing user
    Given I register with name "Jack", surname "Russell", email "jackrussell@gmail.com" and password "norton123" and I log in
    When The customer creates 1 account with 5500.0 euros each
    And The customer make a transfer with their main account and transferAmount 300.0 to an account with id 3456
    Then I should receive a message "Receiver account does not exist"