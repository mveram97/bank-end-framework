package org.example.steps.specific;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.api.data.request.LoginRequest;
import org.junit.Assert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AccountsSteps {
  private Response response;
  private static String jwt;

  @Given("the system is ready and i log with email {string} and password {string}")
  public void theSystemIsReadyAndILogWithEmailAndPassword(String email, String password) {
    RestAssured.baseURI = "http://localhost:8080";

    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setEmail(email);
    loginRequest.setPassword(password);

    response =
        RestAssured.given()
            .contentType("application/json")
            .body(loginRequest)
            .post("/public/login");

    Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    jwt = response.getCookie("cookieToken");
  }

  @When("i request this users account information")
  public void iRequestThisUsersAccountInformation() {
    response =
        RestAssured.given().header(HttpHeaders.COOKIE, "cookieToken=" + jwt).get("/api/accounts");
  }

  @Then("i should receive the code {int}")
  public void iShouldReceiveTheCode(int expectedCode) {
    Assert.assertEquals(expectedCode, response.getStatusCode());
  }

  @When("i request this users account amount")
  public void iRequestThisUsersAccountAmount() {
    response =
        RestAssured.given()
            .header(HttpHeaders.COOKIE, "cookieToken=" + jwt)
            .get("/api/accounts/amount");

    Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
  }

  @Then("i should receive the amount")
  public void iShouldReceiveTheAmount() {
    String amount = response.getBody().asString();
    System.out.println("The amount of the logged user is ".concat(amount).concat(" euros"));
  }
}
