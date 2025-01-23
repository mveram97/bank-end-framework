Feature: Create an account with money and make a transfer to existing account.

  Scenario: Register a new customer and make a transfer to existing customer
    When I register with name "Fernando", surname "Navarro", email "fernav98@hotmail.com" and password "fnav123" and I log in
    And The customer creates 1 account with 1500.0 euros each
    And The customer make a transfer with their main account and transferAmount 500 to an account with id 450
    Then The customer gets a 200 status response and message: "Transfer made successfully"

  Scenario: Register a new customer and make a transfer to existing customer with error
    When I register with name "Fernando", surname "Navarro", email "fernav98@hotmail.com" and password "fnav123" and I log in
    And The customer creates 1 account with 400.0 euros each
    And The customer make a transfer with their main account and transferAmount 500 to an account with id 450
    Then The customer gets a 400 status response and message: "Transfer cannot be completed. Not enough money or blocked receiver."

