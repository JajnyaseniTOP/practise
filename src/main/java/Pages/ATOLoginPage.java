package Pages;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.asis.util.MainClass;

import Driver_manager.DriverManager;

public class ATOLoginPage extends MainClass{	
		private byte[] screenshotBytes;
	@FindBy(xpath="//a[@id='btn-myGovID']")
	private WebElement myGOV;
	@FindBy(xpath= "//input[@type='email']")
	private WebElement emailAddress;
	@FindBy(xpath= "//button[@type='submit']")
	private WebElement loginButton;

	public ATOLoginPage(){	
		PageFactory.initElements(DriverManager.getDriver(), this);       
	}
	public void clickOnMyGOVButton() throws InterruptedException {
		myGOV.click();
	}
	public void sendingEmailAddress() {
		String user_id=ATO_USER_NAME;
		wait.until(ExpectedConditions.elementToBeClickable(emailAddress));
		emailAddress.sendKeys(user_id);
	}
	public byte[] clickOnLoginButton() throws InterruptedException {
		wait.until(ExpectedConditions.elementToBeClickable(loginButton));
		loginButton.click();
		Thread.sleep(3000);
		screenshotBytes = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
		return screenshotBytes;
	}
}