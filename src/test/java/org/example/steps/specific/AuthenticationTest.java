package org.example.steps.specific;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import jakarta.ws.rs.core.NewCookie;
import org.example.api.data.entity.Account;
import org.example.api.data.request.LoginRequest;
import org.example.apicalls.client.BankClient;
import org.example.apicalls.apiconfig.BankAPI;
import jakarta.ws.rs.core.Response;
import org.example.apicalls..Account;
import org.example.apicalls.service.BankService;
import org.example.context.AbstractSteps;
import org.junit.Assert;

import java.util.Map;

public class AuthenticationTest extends AbstractSteps {

    protected BankClient bankClient = new BankClient();
    protected BankAPI proxy;
    protected Response response;
    protected LoginRequest loginRequest = new LoginRequest();
    private BankService bankService = new BankService();

    @When("The customer logins with my email {string} and my password {string}")
    public void theCustomerLoginsWithEmailAndMyPassword(String email,String password){

        response = bankService.doLogin(email,password);

        testContext().setProxy(proxy);
        testContext().setResponse(response);
        testContext().setBankService(bankService);
    }



    @Given("The customer registers with random name, surname, email and password")
    public void theCustomerRegistersWithRandomNameSurnameEmailAndPassword() {
    }
}
