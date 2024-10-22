package Pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;


import com.asis.util.MainClass;

import Driver_manager.DriverManager;

public class ATOCommunicationPage extends MainClass{

	
	@FindBy(xpath="//div[@id='atoo-ahp-atomastermenu-001-3']")
	private WebElement communication;
	@FindBy(xpath= "//span[contains(text(),'Communication')]/parent::div/following-sibling::ul/li[3]")
	private WebElement commHistory;
	
	public ATOCommunicationPage(){
		PageFactory.initElements(DriverManager.getDriver(), this); 
	}
	public void clickCommButton() {
		wait.until(ExpectedConditions.elementToBeClickable(communication));
		communication.click();
	}
	public void clickCommHistoryButton() {
		wait.until(ExpectedConditions.elementToBeClickable(commHistory));
		commHistory.click();
	}
}
