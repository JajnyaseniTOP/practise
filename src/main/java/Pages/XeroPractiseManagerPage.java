
package Pages;

import java.util.ArrayList;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.asis.util.MainClass;

import Driver_manager.DriverManager;

public class XeroPractiseManagerPage extends MainClass{

	@FindBy(xpath="//button[@data-automationid='xnav-appbutton']")
	WebElement client;
	@FindBy(xpath="//a[contains(text(),'Practice Manager')]")
	WebElement practiseManager;
	@FindBy(xpath="//label[contains(text(),'Email address')]/following::input[@id='Code']")
	WebElement emailAdd;
	@FindBy(xpath="//button[contains(text(),'Continue')]")
	WebElement continues;
	
	public XeroPractiseManagerPage() {
		PageFactory.initElements(DriverManager.getDriver(), this);
	}
	public void clickClient() {
		wait.until(ExpectedConditions.elementToBeClickable(client));
		client.click();
	}
	public void clickPractiseManager() {
		wait.until(ExpectedConditions.elementToBeClickable(practiseManager));
		practiseManager.click();
	}
	public void switchingTabs() {
		ArrayList<String> tabs = new ArrayList<>(DriverManager.getDriver().getWindowHandles());
		DriverManager.getDriver().switchTo().window(tabs.get(1));
	}
	public void enterEmailAddress() {
		wait.until(ExpectedConditions.elementToBeClickable(emailAdd));
		emailAdd.sendKeys(XERO_USER_NAME);
	}
	public void enterContinue() {
		wait.until(ExpectedConditions.elementToBeClickable(continues));
		continues.click();
	}
}
