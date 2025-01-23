package org.example.steps.specific;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.ws.rs.core.Response;
import org.example.api.data.entity.Account;
import org.example.api.data.entity.Transfer;
import org.example.api.data.request.TransferRequest;
import org.example.apicalls.apiconfig.BankAPI;
import org.example.apicalls.service.BankService;
import org.example.context.AbstractSteps;
import org.junit.Assert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class PersonalSteps extends AbstractSteps {
  private Response response;
  //private static String jwt;
  private BankService bankService = testContext().getBankService();
  private BankAPI proxy = bankService.proxy;
  private String registeredEmail = testContext().getRegisteredEmail();
  List<Integer> accountsId = new ArrayList<>();


  @And("The customer creates {int} account with {double} euros each and return accountsId")
  public List<Integer> theCustomerCreatesAccountWithEurosEachAndReturnAccountsId(int numberOfAccount, double euros) {

    while (numberOfAccount > 0) {
      Account account = new Account();
      account.setAmount(euros);
      account.setAccountType(Account.AccountType.BUSINESS_ACCOUNT);
      Response accountResponse = bankService.doNewAccount(account, null);

      Assert.assertEquals(201, accountResponse.getStatus());
      numberOfAccount--;
      String accountOrigin = accountResponse.readEntity(String.class);
      String[] parts = accountOrigin.split(": ");

      // Extraer el número como String y luego convertirlo a un número entero
      String accountIdString = parts[1];
      int accountId = Integer.parseInt(accountIdString);
      testContext().setOriginID(accountId);

      accountsId.add(accountId);
    }

    System.out.println(accountsId);

    return accountsId;

  }

  @When("The customer make a transfer with their main account and transferAmount {double} to an account order {int} from another customer")
  public void theCustomerMakeTransferWithTheirMainAccountAndTransferAmountToAnAccountWithStoredOriginId(Double transferAmount, int accountOrder){

    TransferRequest transferRequest = new TransferRequest();
    transferRequest.setOriginAccountId(testContext().getOriginID());
    transferRequest.setTransferAmount(transferAmount);
    transferRequest.setCurrencyType(Transfer.CurrencyType.EUR);
    System.out.println(accountsId.get(accountOrder-1));
    transferRequest.setReceivingAccountId(accountsId.get(accountOrder-1));

    Response transferResponse= bankService.doNewTransfer(transferRequest,null);

    testContext().setResponse(transferResponse);
  }

}