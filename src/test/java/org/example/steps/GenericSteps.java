package org.example.steps;  // Asegúrate de que el paquete sea correcto

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import static org.junit.Assert.assertEquals;

public class GenericSteps {
  private Response response;

  @When("I send a {string} to {string}")
  public void iSendATo(String method, String url) {
    if (method.equalsIgnoreCase("POST")) {
      response = RestAssured.given()
              .header("Content-Type", "application/json")
              .body("{ \"name\": \"morpheus\", \"job\": \"leader\" }")  // Cuerpo de la solicitud para POST
              .post(url);
    } else if (method.equalsIgnoreCase("GET")) {
      response = RestAssured.given()
              .header("Content-Type", "application/json")
              .get(url);  // No se incluye body para GET
    }
  }

  @Then("Result should be {int}")
  public void resultShouldBe(int expectedStatusCode) {
    assertEquals(expectedStatusCode, response.getStatusCode());
  }
}
