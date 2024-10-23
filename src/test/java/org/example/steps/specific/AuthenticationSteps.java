package org.example.steps.specific;

import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import jakarta.ws.rs.core.Response;
import org.example.api.data.entity.Customer;
import org.example.apicalls.apiconfig.BankAPI;
import org.example.apicalls.service.BankService;
import org.example.context.AbstractSteps;
import org.junit.Assert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AuthenticationSteps extends AbstractSteps {

    private Response response;
    public static BankService bankService = new BankService();
    private  String registeredEmail = testContext().getRegisteredEmail();
    private final String baseUrl = "http://localhost:8080";
    private static BankAPI proxy = bankService.proxy;
    @Given("the system is ready and i log with email {string} and password {string}")
    public void theSystemIsReadyAndILogWithEmailAndPassword(String email, String password) {
        response = bankService.doLogin(email,password);

        testContext().setResponse(response);
        testContext().setBankService(bankService);
    }

    @Given("the system is ready for user authentication")
    public void systemIsReady() {
        RestAssured.baseURI = baseUrl;
    }

    @When("I register with name {string}, surname {string}, email {string} and password {string}")
    public void registerUser(String name, String surname, String email, String password) {
        registeredEmail = email;
        response = bankService.doRegister(name, surname, email, password);
    }

    @Then("I should receive a message {string}")
    public void verifyMessage(String expectedMessage) {
        String actualMessage = response.readEntity(String.class);
        Assert.assertEquals(expectedMessage, actualMessage);
    }

    @Given("I have registered with name {string}, surname {string}, email {string} and password {string}")
    public void registerForLogin(String name, String surname, String email, String password) {
        registeredEmail = email;
        response = bankService.doRegister(name,surname,email,password);
    }

    @When("I login with email {string} and password {string}")
    public void loginUser(String email, String password) {
        response = bankService.doLogin(email,password);
        testContext().setResponse(response);
        testContext().setBankService(bankService);
    }

    @Given("I have logged in with email {string} and password {string}")
    public void loginUserForLogout(String email, String password) {
        response = bankService.doLogin(email,password);
        testContext().setResponse(response);
        testContext().setBankService(bankService);
    }

    @When("I log out")
    public void logoutUser() {
        response = bankService.doLogout();
    }

    @When("The customer logins with  email {string} and  password {string}")
    public void theCustomerLoginsWithEmailAndMyPassword(String email,String password){

        response = bankService.doLogin(email,password);
        testContext().setResponse(response);
        testContext().setBankService(bankService);
    }

    @When("The customer logins")
    public void theCustomerLogins() {
        String email = testContext().getCustomer().getEmail();
        String password = testContext().getCustomer().getPassword();
        response = bankService.doLogin(email,password);
        testContext().setBankService(bankService);
        Assert.assertEquals(200,response.getStatus());
    }

    @After
    public void deleteRegisteredUser() {
    System.out.println(registeredEmail);
        if (registeredEmail != null) {
            Response deleteResponse = proxy.deleteCustomer(registeredEmail);

            int statusCode = deleteResponse.getStatus();
            System.out.println("Delete response status code: " + statusCode);
            Assert.assertEquals(HttpStatus.OK.value(), statusCode);  // Validar si realmente devolvió un 200 OK

            registeredEmail = null;
        } else {
            System.out.println("No user to delete, registeredEmail is null");
        }
    }


}
