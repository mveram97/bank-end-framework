package org.example.steps.specific;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import jakarta.ws.rs.core.NewCookie;
import org.example.api.data.request.LoginRequest;
import org.example.apicalls.client.BankClient;
import org.example.apicalls.apiconfig.BankAPI;
import jakarta.ws.rs.core.Response;
import org.example.context.AbstractSteps;

import java.util.Map;

public class AuthenticationTest extends AbstractSteps {

    protected BankClient bankClient = new BankClient();
    protected BankAPI proxy;
    protected Response response;
    protected LoginRequest loginRequest = new LoginRequest();


    @When("The customer logins with my email {string} and my password {string}")
    public void theCustomerLoginsWithEmailAndMyPassword(String email,String password){
        proxy = bankClient.getAPI();

        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        response = proxy.login(loginRequest, null);

        if (response.getStatus() == 200){
            Map<String, NewCookie> cookies = response.getCookies();
            NewCookie newCookie = cookies.entrySet().iterator().next().getValue();
            proxy = bankClient.getAPI(newCookie);
        }


        testContext().setProxy(proxy);
        testContext().setResponse(response);
    }

    @Given("The customer registers with random name, surname, email and password")
    public void theCustomerRegistersWithRandomNameSurnameEmailAndPassword() {
    }
}
