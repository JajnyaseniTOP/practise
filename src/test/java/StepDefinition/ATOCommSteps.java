package StepDefinition;

import com.asis.util.MainClass;

import Pages.ATOCommunicationPage;
import io.cucumber.java.en.*;

public class ATOCommSteps extends MainClass{

	public ATOCommunicationPage comm = new ATOCommunicationPage();
	@Given("User click on communication button")
	public void user_click_on_communication_button() {
		comm.clickCommButton();
	}

	@Then("User click on communiction History")
	public void user_click_on_communiction_history() {
		comm.clickCommHistoryButton();
	}
}
