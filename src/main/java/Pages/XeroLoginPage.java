
package Pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.asis.util.MainClass;

import Driver_manager.DriverManager;

public class XeroLoginPage extends MainClass{

	@FindBy(id = "xl-form-email")
	WebElement Emailaddress;
	@FindBy(id = "xl-form-password")
	WebElement Password;
	@FindBy(id = "xl-form-submit")
	WebElement loginButton;
	@FindBy(xpath = "//button[contains(text(),'Use another authentication method')]")
	WebElement anotherAuthMethod;
	@FindBy(xpath = "//h2[contains(text(),'Security questions')]")
	WebElement securityQsn;

	// Constructor
	public XeroLoginPage() {
		PageFactory.initElements(DriverManager.getDriver(), this);
	}
	public void getPageTitle() {
	}
	public void enterUserId() {
        wait.until(ExpectedConditions.elementToBeClickable(Emailaddress));
        Emailaddress.sendKeys(XERO_USER_NAME);
    }

    public void enterPassword() {
        wait.until(ExpectedConditions.elementToBeClickable(Password));
        Password.sendKeys(XERO_PASSWORD);
    }
	public void clickLoginButton() {
		wait.until(ExpectedConditions.elementToBeClickable(loginButton));
		loginButton.click();
	}
	public void clickAnotherAuthMethod() {
		wait.until(ExpectedConditions.elementToBeClickable(anotherAuthMethod));
		anotherAuthMethod.click();
	}
	public void clickSecurityQsn() {
		wait.until(ExpectedConditions.elementToBeClickable(securityQsn));
		securityQsn.click();
	}
}
