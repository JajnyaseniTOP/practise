
package StepDefinition;

import Pages.XeroPractiseManagerPage;
import io.cucumber.java.en.*;

public class XeroPractiseManagerSteps {
	
	XeroPractiseManagerPage practise = new XeroPractiseManagerPage();

	@Given("User click on client")
	public void user_click_on_client() {
		practise.clickClient();
	}

	@When("User click on Practise manager")
	public void user_click_on_practise_manager() {
		practise.clickPractiseManager();
	}

	@Then("User switch the tab")
	public void user_switch_the_tab() {
		practise.switchingTabs();
		practise.enterEmailAddress();
		practise.enterContinue();
	}
}
