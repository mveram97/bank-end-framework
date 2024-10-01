package steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class GenericSteps {

  @When("I send a {string} to {string}")
  public void iSendATo(String arg0, String arg1) {}

  @Then("Result should be {int}")
  public void resultShouldBe(int arg0) {}
}
