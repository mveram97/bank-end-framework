package org.example.steps.specific;

import io.cucumber.java.AfterAll;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.api.data.request.LoginRequest;
import org.example.api.data.entity.Customer;
import org.junit.Assert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.example.apicalls.dto.CustomerDTO;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AuthenticationSteps {


    private static String registeredEmail;
    private Response response;
    private final String baseUrl = "http://localhost:8080";

    @Given("the system is ready for user authentication")
    public void systemIsReady() {
        RestAssured.baseURI = baseUrl;
    }

    @When("I register with name {string}, surname {string}, email {string} and password {string}")
    public void registerUser(String name, String surname, String email, String password) {
    CustomerDTO customer = new CustomerDTO();
        customer.setName(name);
        customer.setSurname(surname);
        customer.setEmail(email);
        customer.setPassword(password);
        registeredEmail = email;
        System.out.println("HEEEEEEEEEEEEEEEEEEEEEEEEEE " +registeredEmail);

        response = RestAssured.given()
                .contentType("application/json")
                .body(customer)
                .post("/public/register");
    }

    @Then("I should receive a message {string}")
    public void verifyMessage(String expectedMessage) {
        String actualMessage = response.getBody().asString();
        Assert.assertEquals(expectedMessage, actualMessage);
    }

    @Given("I have registered with name {string}, surname {string}, email {string} and password {string}")
    public void registerForLogin(String name, String surname, String email, String password) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setSurname(surname);
        customer.setEmail(email);
        customer.setPassword(password);

        RestAssured.given()
                .contentType("application/json")
                .body(customer)
                .post("/public/register");
    }

    @When("I login with email {string} and password {string}")
    public void loginUser(String email, String password) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        response = RestAssured.given()
                .contentType("application/json")
                .body(loginRequest)
                .post("/public/login");
    }

    @Given("I have logged in with email {string} and password {string}")
    public void loginUserForLogout(String email, String password) {
        loginUser(email, password);
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @When("I log out")
    public void logoutUser() {
        System.out.println(registeredEmail + " Hewwwwwwwwwwy");
        String jwt = response.getCookie("cookieToken");

        response = RestAssured.given()
                .header(HttpHeaders.COOKIE, "cookieToken=" + jwt)
                .post("/public/logout");
    }

    @AfterAll
    public static void deleteRegisteredUser() {
    System.out.println(registeredEmail);
        if (registeredEmail != null) {
            Response deleteResponse = RestAssured.given()
                    .delete("/public/customer/" + registeredEmail);

            int statusCode = deleteResponse.getStatusCode();
            System.out.println("Delete response status code: " + statusCode);
            Assert.assertEquals(HttpStatus.OK.value(), statusCode);  // Validar si realmente devolvió un 200 OK

            registeredEmail = null;
        } else {
            System.out.println("No user to delete, registeredEmail is null");
        }
    }


}
