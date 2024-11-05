package StepDefinition;

import Pages.ATOcommHistoryExtarctionPage;
import io.cucumber.java.en.*;

public class ATOClickLinkSteps {

	public ATOcommHistoryExtarctionPage filePage = new ATOcommHistoryExtarctionPage();
	
	@Given("The user navigates through 100 pages")
	public void user_click_on_pages() throws InterruptedException {
		filePage.clickDownloadButton();
		
	}

	@When("The user clicks on the download button and handles the download pop-up")
	public void user_have_list_of_link_having_notice_of_assessment() {
		filePage.clickPopUp();
	}

	@Then("The user clicks on each link in the table to download the corresponding files")
	public void user_click_on_list_of_link_having_notice_of_assessment() throws InterruptedException {
		filePage.extractCommTableStatement();
		filePage.clickAllLinks();
		filePage.closeBrowser();
	}
}
