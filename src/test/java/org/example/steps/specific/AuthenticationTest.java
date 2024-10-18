package org.example.steps.specific;

import io.cucumber.java.en.When;
import org.example.api.data.request.LoginRequest;
import org.example.apicalls.client.BankClient;
import org.example.apicalls.apiconfig.BankAPI;
import jakarta.ws.rs.core.Response;
import org.example.context.AbstractSteps;

public class AuthenticationTest extends AbstractSteps {

    protected BankClient bankClient = new BankClient();
    protected BankAPI proxy;
    protected Response response;
    protected LoginRequest loginRequest = new LoginRequest();


    @When("I login with my email {string} and my password {string}")
    public void loginWithEmailAndMyPassword(String email,String password){
        proxy = bankClient.getAPI();

        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        response = proxy.login(loginRequest, null);
        testContext().setResponse(response);
    }
}
