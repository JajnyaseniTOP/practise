package StepDefinition;

import com.asis.util.MainClass;

import Pages.XeroSearchClientPage;
import Pages.XeroSearchClientPageAsis;
import Pages.XeroSearchClientPageJJ;
import Pages.XeroSearchClientPageJJRenew;
import io.cucumber.java.en.*;

public class XeroSearchClientSteps extends MainClass {

	private XeroSearchClientPageJJRenew search = new XeroSearchClientPageJJRenew();

	@Given("I am on the Xero search client page")
	public void i_am_on_the_Xero_search_client_page() {

	}

	@When("I input the client name")
	public void i_input_the_client_name() {

		search.clickOnSearchButton();
	}

	@When("I click on the search button")
	public void i_click_on_the_search_button() throws InterruptedException {
		search.inputTheClientName();
		Thread.sleep(2000);
		//search.inputTheClientNameInOthrPortal();
		//		search.checkNoticeOfAssessment(filePath,downloadDir);

	}


	@Then("I should see the client code")
	public void i_should_see_the_client_code() throws InterruptedException {
		search.renameAndMovePdfFilesToDownloadsFolder(downloadDir);
	}

	@Then("I should see the client email")
	public void i_should_see_the_client_email() {
		//		search.getClientEmail();
	}
}
