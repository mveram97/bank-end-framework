package org.example.steps;

import io.cucumber.java.en.Then;
import jakarta.ws.rs.core.Response;
import org.example.context.AbstractSteps;
import org.junit.Assert;

import static org.junit.Assert.assertEquals;

public class GenericSteps extends AbstractSteps {
    private Response response;

    @Then("I get a {int} status response and message: {string}")
    public void iGetStatusResponseAndBody(Integer expectedStatus, String expectedMessage){
        //Se recibe la respuesta y se extrae el mensaje y el status de la response
        response = testContext().getResponse();
        Integer receivedStatus = response.getStatus();
        String receivedMessage= response.readEntity(String.class);

        // Comprobamos que el status y el mensaje de la response sean los esperados
        Assert.assertEquals(expectedStatus,receivedStatus);
        Assert.assertEquals(expectedMessage,receivedMessage);

        // Reseteamos el contexto (Esto deberia añadirse en un @AfterEach para asegurarse que los test vayan bien)
        testContext().reset();
    }
}
