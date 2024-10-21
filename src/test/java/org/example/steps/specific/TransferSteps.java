package org.example.steps.specific;

import io.cucumber.java.en.When;
import jakarta.ws.rs.core.Response;
import org.example.api.data.entity.Transfer;
import org.example.api.data.request.TransferRequest;
import org.example.apicalls.apiconfig.BankAPI;
import org.example.apicalls..Transfer;
import org.example.apicalls.service.BankService;
import org.example.context.AbstractSteps;

public class TransferSteps extends AbstractSteps {
    private BankAPI proxy = testContext().getProxy();
    private BankService bankService = testContext().getBankService();
    @When("The customer make a transfer with their main account and transferAmount {double} to an account with id {int}")
    public void theCustomerMakeTransferWithTheirMainAccountAndTransferAmountToAnAccountWithId(Double transferAmount, int receiverAccountId){

        Transfer transfer = new Transfer();
        transfer.setOriginAccountId(testContext().getOriginID());
        transfer.setTransferAmount(transferAmount);
        transfer.setCurrencyType("EUR");
        transfer.setReceivingAccountId(receiverAccountId);

        Response transferResponse= bankService.doNewTransfer(transfer,null);

        testContext().setResponse(transferResponse);
    }


}
