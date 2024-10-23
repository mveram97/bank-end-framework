package org.example.steps.specific;

import io.cucumber.java.en.And;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import org.example.apicalls.apiconfig.BankAPI;
import org.example.apicalls.service.BankService;
import org.example.context.AbstractSteps;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class CardSteps extends AbstractSteps {
    private Response response;
    private BankService bankService = testContext().getBankService();
    private BankAPI proxy = bankService.proxy;
    @And("The customer checks their cards")
    public void theCustomerChecksTheirCards(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        response = proxy.getCards(securityContext);
        testContext().setResponse(response);
    }
}
