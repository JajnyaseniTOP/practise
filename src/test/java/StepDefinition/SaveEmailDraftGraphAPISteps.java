package StepDefinition;

import Pages.SaveEmailDraftGraphAPI_New;
import io.cucumber.java.en.*;
import com.asis.util.MainClass;


public class SaveEmailDraftGraphAPISteps extends MainClass{

	SaveEmailDraftGraphAPI_New email = new SaveEmailDraftGraphAPI_New();

    @When("I run the SaveEmailDraftGraphAPI program")
    public void iRunTheSaveEmailDraftGraphAPIProgram() throws Exception {
    	email.saveEmailsAsDraftsFromExcel(filePath);
    	email.closeBrowserXero();
    }

}