package org.example.steps;

import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.AfterStep;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import org.example.api.data.request.LoginRequest;
import org.example.api.data.entity.Customer;
import org.example.apicalls.apiconfig.BankAPI;
import org.example.apicalls.client.BankClient;
import org.example.apicalls.service.BankService;
import org.junit.Assert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.Map;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AuthenticationSteps {
    private static String registeredEmail;
    BankService bankService = new BankService();
    private Response response;
    private final String baseUrl = "http://localhost:8080";

    @Given("the system is ready for user authentication")
    public void systemIsReady() {
        RestAssured.baseURI = baseUrl;
    }

    @When("I register with name {string}, surname {string}, email {string} and password {string}")
    public void registerUser(String name, String surname, String email, String password) {
        response = bankService.doRegister(name, surname, email, password);
    }

    @Then("I should receive a message {string}")
    public void verifyMessage(String expectedMessage) {
        String actualMessage = response.readEntity(String.class);
        Assert.assertEquals(expectedMessage, actualMessage);
    }

    @Given("I have registered with name {string}, surname {string}, email {string} and password {string}")
    public void registerForLogin(String name, String surname, String email, String password) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setSurname(surname);
        customer.setEmail(email);
        customer.setPassword(password);

        response = bankService.doLogin(customer);
    }

    @When("I login with email {string} and password {string}")
    public void loginUser(String email, String password) {
        response = bankService.doLogin(email, password);
    }

    @Given("I have logged in with email {string} and password {string}")
    public void loginUserForLogout(String email, String password) {
        loginUser(email, password);
    }

    @When("I log out")
    public void logoutUser() {
        response = bankService.doLogout();
    }

    @AfterAll
    public static void deleteRegisteredUser() {
    System.out.println(registeredEmail);
        if (registeredEmail != null) {
            Response deleteResponse = (Response) RestAssured.given()
                    .delete("/public/customer/" + registeredEmail);

            int statusCode = deleteResponse.getStatus();
            System.out.println("Delete response status code: " + statusCode);
            Assert.assertEquals(HttpStatus.OK.value(), statusCode);  // Validar si realmente devolvió un 200 OK

            registeredEmail = null;
        } else {
            System.out.println("No user to delete, registeredEmail is null");
        }
    }

}
