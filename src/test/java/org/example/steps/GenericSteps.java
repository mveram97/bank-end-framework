package org.example.steps;  // Asegúrate de que el paquete sea correcto

import io.cucumber.java.en.And;
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
    }
    else if (method.equalsIgnoreCase("GET")){
      response = RestAssured.get(url);
    }
    else if (method.equalsIgnoreCase("DELETE")){
      response = RestAssured.delete(url);
    }
  }

  @Then("Result should be {int}")
  public void resultShouldBe(int expectedStatusCode) {
    assertEquals(expectedStatusCode, response.getStatusCode());
  }

  @And("I have to wait {int} seconds")
  public void iHaveToWaitSeconds(int seconds) {
    try {
      Thread.sleep(seconds * 1000); // Espera el número de segundos especificado
    } catch (InterruptedException e) {
      // Manejo de la excepción si el hilo es interrumpido
      e.printStackTrace();
    }
  }

  @When("I send a {string} to {string} with  {string} and {string}")
  public void iSendAToWithAnd(String method, String url, String email, String password) {
    if (method.equalsIgnoreCase("POST")) {
      response = RestAssured.given()
              .header("Content-Type", "application/json")
              .body("{ \"email\": \""+ email + "\", \"password\": \""+ password +"\" }")
              .post(url);

    }
  }
}
