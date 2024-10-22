package StepDefinition;

import com.asis.util.MainClass;

import Pages.TaxReturnPage;
import io.cucumber.java.en.*;

public class XeroTaxReturnSteps  extends MainClass{
	
	TaxReturnPage tax = new TaxReturnPage();
	@Given("the system processes each client with Notice of Assessment")
	public void the_system_processes_each_client_with_notice_of_assessment() {
		TaxReturnPage.processAllNoticesOfAssessment(filePath,downloadDir);
	}

	@Then("it should extract the Date of Issue, ATO Reference, Taxable Income, and Result Amount from the PDF")
	public void it_should_extract_the_date_of_issue_ato_reference_taxable_income_and_result_amount_from_the_pdf() {
	}

	@Then("it should fill the Date of Issue, ATO Reference, Taxable Income, and Result Amount in the web form for each client")
	public void it_should_fill_the_date_of_issue_ato_reference_taxable_income_and_result_amount_in_the_web_form_for_each_client() {
	}

}
