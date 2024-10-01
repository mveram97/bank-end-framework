package org.example.steps;  // Asegúrate de que el paquete sea correcto

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import static org.junit.Assert.assertEquals;

public class GenericSteps {
  private Response response;

  @Given("A user with ID {int} exists")
  public void aUserWithIDExists(int userId) {
    response = RestAssured.get("https://reqres.in/api/users/" + userId);
    assertEquals(200, response.getStatusCode());  // Asegurarse de que el usuario existe
  }

  @When("I send a {string} to {string}")
  public void iSendATo(String method, String url) {
    if (method.equalsIgnoreCase("POST")) {
      response = RestAssured.given()
              .header("Content-Type", "application/json")
              .body("{ \"name\": \"morpheus\", \"job\": \"leader\" }")
              .post(url);

    }
    else if (method.equalsIgnoreCase("PUT")) {
      response = RestAssured.given()
              .header("Content-Type", "application/json")
              .body("{ \"name\": \"morpheus\", \"job\": \"zion resident\" }")  // Datos de actualización
              .put(url);
    } else if (method.equalsIgnoreCase("GET")) {
      response = RestAssured.given()
              .header("Content-Type", "application/json")
              .get(url);

    } else if (method.equalsIgnoreCase("DELETE")) {
      response = RestAssured.given()
              .header("Content-Type", "application/json")
              .delete(url);
    }
  }

  @Then("Result should be {int}")
  public void resultShouldBe(int expectedStatusCode) {
    assertEquals(expectedStatusCode, response.getStatusCode());
  }
}
