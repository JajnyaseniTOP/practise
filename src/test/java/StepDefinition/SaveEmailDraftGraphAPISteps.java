package StepDefinition;


import Pages.SaveEmailDraftGraphAPI;
import io.cucumber.java.en.*;

import com.asis.util.MainClass;


public class SaveEmailDraftGraphAPISteps extends MainClass{

    SaveEmailDraftGraphAPI email = new SaveEmailDraftGraphAPI();

    @When("I run the SaveEmailDraftGraphAPI program")
    public void iRunTheSaveEmailDraftGraphAPIProgram() throws Exception {
    	email.saveEmailsAsDraftsFromExcel(filePath, downloadDir);
    	email.closeBrowserXero();
    }

}