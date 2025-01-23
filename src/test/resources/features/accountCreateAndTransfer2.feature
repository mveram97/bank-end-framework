Feature: Create an account with money and make a transfer to existing account.

  Scenario: Register a two customers, create accounts and make transfer between them
    Given I register with name "Fernando", surname "Navarro", email "fernav99@hotmail.com" and password "fnav123" and I log in
    And The customer creates 1 account with 300.0 euros each and return accountsId
    #And I log out
    And I register with name "Pepe", surname "Gallardo", email "pepegall00@gmail.com" and password "pgallardo123" and I log in
    And The customer creates 1 account with 1500.0 euros each
    When The customer make a transfer with their main account and transferAmount 500 to an account order 1 from another customer
    Then The customer gets a 200 status response and message: "Transfer made successfully"


