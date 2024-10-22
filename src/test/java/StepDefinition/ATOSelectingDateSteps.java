package StepDefinition;

import Pages.ATOSelectingDatePage;
import io.cucumber.java.en.*;

public class ATOSelectingDateSteps {

	public ATOSelectingDatePage date = new ATOSelectingDatePage();
	
	@Given("User click on search option")
	public void user_click_on_search_option() {
		date.clickDateOption();
	}

	@When("User click on last {int} hours")
	public void user_click_on_last_hours(Integer int1) {
		date.selectDate();
	}

	@When("User unselect SMS option")
	public void user_unselect_sms_option() {
		date.clickSMS();
	}

	@Then("User click on search button")
	public void user_click_on_search_button() {
		date.clickSearchButton();
	}
}
