package org.example.steps.specific;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import org.example.api.data.entity.Account;
import org.example.api.data.request.LoginRequest;
import org.example.apicalls.apiconfig.BankAPI;
import org.example.apicalls.dto.AccountDTO;
import org.example.context.AbstractSteps;
import org.junit.Assert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AccountsSteps extends AbstractSteps {
  private Response response;
  private static String jwt;
  private BankAPI proxy = testContext().getProxy();

  @Given("the system is ready and i log with email {string} and password {string}")
  public void theSystemIsReadyAndILogWithEmailAndPassword(String email, String password) {
    RestAssured.baseURI = "http://localhost:8080";

    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setEmail(email);
    loginRequest.setPassword(password);

    response =
            (Response) RestAssured.given()
                .contentType("application/json")
                .body(loginRequest)
                .post("/public/login");

    Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());
    Map<String, NewCookie> cookies = response.getCookies();
    NewCookie newCookie = cookies.entrySet().iterator().next().getValue();
    jwt = newCookie.getValue();
  }

  @When("i request this users account information")
  public void iRequestThisUsersAccountInformation() {
    response =
            (Response) RestAssured.given().header(HttpHeaders.COOKIE, "cookieToken=" + jwt).get("/api/accounts");
  }

  @Then("i should receive the code {int}")
  public void iShouldReceiveTheCode(int expectedCode) {
    Assert.assertEquals(expectedCode, response.getStatus());
  }

  @When("i request this users account amount")
  public void iRequestThisUsersAccountAmount() {
    response =
            (Response) RestAssured.given()
                .header(HttpHeaders.COOKIE, "cookieToken=" + jwt)
                .get("/api/accounts/amount");

    Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());
  }

  @Then("i should receive the amount")
  public void iShouldReceiveTheAmount() {
    String amount = response.readEntity(String.class);
    System.out.println("The amount of the logged user is ".concat(amount).concat(" euros"));
  }


  @And("The customer creates {int} account with {double} euros each")
  public void theCustomerCreatesAccountWithEurosEach(int numberOfAccount, double euros) {

    while(numberOfAccount>0) {
      AccountDTO account = new AccountDTO();
      account.setAmount(euros);
      account.setAccountType(Account.AccountType.BUSINESS_ACCOUNT);
      Response accountResponse = (Response) proxy.createAccount(account, null);

      Assert.assertEquals(201,accountResponse.getStatus());
      numberOfAccount--;
      String accountOrigin = accountResponse.readEntity(String.class);
      String[] parts = accountOrigin.split(": ");

      // Extraer el número como String y luego convertirlo a un número entero
      String accountIdString = parts[1];
      int accountId = Integer.parseInt(accountIdString);
      testContext().setOriginID(accountId);
    }

  }

  @And("The receiving customer has an account with id {int}")
  public void theReceivingCustomerHasAnAccountWithId(int receiverAccountId) {
    Response receiverAccountresponse = proxy.accountById(receiverAccountId);
    Assert.assertEquals(200,receiverAccountresponse.getStatus());
  }
}
