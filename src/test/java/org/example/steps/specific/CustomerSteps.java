package org.example.steps.specific;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import org.example.api.data.entity.Customer;
import org.example.api.data.request.UpdateRequest;
import org.example.api.service.AuthService;
import org.example.api.service.CustomerService;
import org.example.apicalls.apiconfig.BankAPI;
import org.example.apicalls.client.BankClient;
import org.example.apicalls.service.BankService;
import org.example.context.AbstractSteps;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CustomerSteps extends AbstractSteps {
    private BankService bankService = testContext().getBankService();
    public BankClient client = new BankClient();
    @Autowired private CustomerService customerService;
    private BankAPI proxy = bankService.proxy;
    private Customer randomCustomer;  // Mantén el cliente como estado de la clase

    @Given("The customer registers with random email, name, surname and password")
    public void registerRandomCustomer() {
        randomCustomer = bankService.registerRandomCustomer();  // Almacena el cliente generado
        System.out.println("Customer registered: " + randomCustomer.getEmail());
        assertNotNull(randomCustomer.getEmail());
    }

    @And("The customer logging with the register credentials")
    public void theCustomerLogins(){
        String email = randomCustomer.getEmail();
        String password = randomCustomer.getPassword();
        Response response = bankService.doLogin(email,password);
        testContext().setBankService(bankService);
        assertEquals(200,response.getStatus());
    }

    @When("The customer updates their name to {string} and surname {string}")
    public void updateCustomerNameAndSurname(String name, String surname) {
        proxy = bankService.proxy;
        assertNotNull(randomCustomer);

        UpdateRequest nameUpdateRequest = new UpdateRequest();
        nameUpdateRequest.setName(name);
        nameUpdateRequest.setSurname(surname);

        Response response = proxy.updateNameAndSurname(nameUpdateRequest, null);
        System.out.println("Customer name updated to: " + name + " " + surname);
        System.out.println(response.readEntity(String.class));
    }

    @And("The customer updates their email to {string} and password to {string}")
    public void updateCustomerEmailAndPassword(String email, String password) {
        proxy = bankService.proxy;
        assertNotNull(randomCustomer);
        randomCustomer.setEmail(email);
        // Crear el request para actualizar email y contraseña
        UpdateRequest emailPasswordUpdateRequest = new UpdateRequest();
        emailPasswordUpdateRequest.setEmail(email);
        emailPasswordUpdateRequest.setPassword(password);

        try {
            Response responseEmail = proxy.updateEmail(emailPasswordUpdateRequest, null);
            System.out.println(responseEmail.readEntity(String.class));
            Map<String, NewCookie> cookies = responseEmail.getCookies();
            NewCookie newCookie = cookies.entrySet().iterator().next().getValue();
            proxy = client.getAPI(newCookie);

            if (responseEmail.getStatus() == 200) {
                Response responsePassword = proxy.updatePassword(emailPasswordUpdateRequest, null);
                System.out.println(responsePassword.readEntity(String.class));
                if (responsePassword.getStatus() == 200) {
                    System.out.println("User credentials updated successfully.");         }
                else {
                    System.out.println("Failed to update password: " + responsePassword.getStatusInfo());
                }
            }
            else {
                System.out.println("Failed to update email: " + responseEmail.getStatusInfo());
            }
        }
        catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage()); }
    }

    @Then("The customer’s name, surname, email and password have been updated {string}")
    public void verifyCustomerUpdated(String updateStatus) {
        assertNotNull(randomCustomer);

        Optional<Customer> updatedCustomer = customerService.findByEmail(randomCustomer.getEmail());

        if (updateStatus.equals("successfully")) {
            assertTrue(updatedCustomer.isPresent());
            System.out.println("Customer updated successfully with email: " + updatedCustomer.get().getEmail());
        } else {
            assertFalse(updatedCustomer.isPresent());
            System.out.println("Customer update failed.");
        }
    }
}
