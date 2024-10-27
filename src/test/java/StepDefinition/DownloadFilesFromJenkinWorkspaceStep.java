package StepDefinition;

import Pages.DownloadFilesFromJenkinWorkspace;
import io.cucumber.java.en.Given;

public class DownloadFilesFromJenkinWorkspaceStep {
	DownloadFilesFromJenkinWorkspace  downloadFilesFromJenkinWorkspace= new DownloadFilesFromJenkinWorkspace();
	
	@Given("I download the file from Jenkins workspace")
	public void i_input_the_client_name() {
		downloadFilesFromJenkinWorkspace.downloadFileFromJenkins();
	}
}
