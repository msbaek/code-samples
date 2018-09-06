package specs;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class StepDefinitions {

    private Belly belly;

    @Given("I have (\\d+) cukes in my belly")
    public void i_have_cukes_in_my_belly(int cukes) {
        belly = new Belly();
        belly.eat(cukes);
    }

    @When("I wait (\\d+) hour")
    public void i_wait_hour(int hours) {
        belly.wait(hours);
    }

    @Then("my belly should growl")
    public void my_belly_should_growl() {
        assertThat(belly.growl(), is(true));
    }
}
