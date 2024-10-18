Feature: Check Transfer Not Enough Money in Sender User

  Scenario Outline: A customer with an account with less than 1000€ does a transfer to another user of 1500€
    Given The customer logins with my email "csan@email.com" and my password "2334"
    And The customer creates <numberOfAccounts> account with <money> euros each
    And The receiving customer has an account with id <receiverAccountId>
    When The customer make a transfer with their main account and transferAmount <transferAmount> to an account with id <receiverAccountId>
    Then The customer gets a <status> status response and message: <message>

    Examples:
    |numberOfAccounts|money   |receiverAccountId    |transferAmount     |status   |message                                                                  |
    |1               |800     |1                    |1500               |400      |"Transfer can not be done. Not enough money or blocked receiver."        |
