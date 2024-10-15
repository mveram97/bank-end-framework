package org.example.steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.boot.test.context.SpringBootTest;
import org.example.api.Application;

import java.net.URL;

import static org.junit.Assert.assertEquals;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,classes = Application.class) // Aquí puedes especificar la clase de configuración si es necesario
public class CucumberSpringConfiguration {
    private Response response;

    @When("El usuario inicia sesión con email {string} y contraseña {string}")
    public void elUsuarioIniciaSesionConEmailyContraseña(String email, String password){
        try{
            URL url = new URL("http://localhost:8080/public/login");
            String jsonBody = "{ \"email\": \""+email+"\", \"password\": \""+password+"\" }";

            response = RestAssured.given()
                    .header("Content-Type", "application/json")
                    .body(jsonBody)  // Datos de actualización
                    .post(url);

        } catch (Exception e){
            e.printStackTrace();
        }
    }


    @Then("Devuelve un codigo {int}")
    public void devuelveUnCodigo(int expectedStatusCode) {
        assertEquals(expectedStatusCode,response.getStatusCode());
    }
}

