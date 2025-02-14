package Pages;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.asis.util.ClientExcel;
import com.asis.util.MainClass;
import Driver_manager.DriverManager;
import io.netty.handler.timeout.TimeoutException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
public class ATOcommHistoryExtarctionPage extends MainClass {
	ClientExcel creator = new ClientExcel();
	@FindBy(xpath = "//button[@title='Next page']")
	private WebElement next;
	@FindBy(xpath = "//button[@title='Previous page']")
	private WebElement next2;
	@FindBy(xpath = "//button[@title='Download']")
	private WebElement download;
	@FindBy(xpath = "//button[contains(text(),'Yes')]")
	private WebElement yesPopUp;
	@FindBy(xpath = "//table//tr//td[position()=2]//a")
	private List<WebElement> links;
	@FindBy(xpath = "//table//tr//td[position()=2]//a")
	private List<WebElement> links1;
	@FindBy(xpath = "//tbody/tr[@class=\"table-row\"]")
	private List<WebElement> commTableHistory;
	@FindBy(xpath = "//th[@data-header='Name']")
	private List<WebElement> clientName;
	@FindBy(xpath="//select[@id='dd-atoo-cch-results-per-page-001']")
	private WebElement resultPerPage;
	@FindBy(xpath="//option[contains(text(),'50')]")
	private WebElement pages100;
	public ATOcommHistoryExtarctionPage() {
		PageFactory.initElements(DriverManager.getDriver(), this);
	}
	public void clickDownloadButton() throws InterruptedException {
		wait.until(ExpectedConditions.elementToBeClickable(download));
		Thread.sleep(9000);
		download.click();
	}
	public void clickPopUp() {
		wait.until(ExpectedConditions.elementToBeClickable(yesPopUp));
		yesPopUp.click();
	}
	public ArrayList<ArrayList<String>> extractCommTableStatement() throws InterruptedException {
		creator.createEmptyExcelSheet(); 
		Thread.sleep(5000);
		for (WebElement tr : commTableHistory) {
			if (tr.isDisplayed()) {
				List<WebElement> tdData = tr.findElements(By.xpath(".//td | .//th"));
				ArrayList<String> tdRowData = new ArrayList<>();
				for (WebElement td : tdData) {
					tdRowData.add(td.getText());
				}
				ACTIVITY_STATEMENT_DATA.add(tdRowData);
			}
		}
		ClientExcel.writeDataToExcel(ACTIVITY_STATEMENT_DATA);
		return ACTIVITY_STATEMENT_DATA;
	}
	public ArrayList<ArrayList<String>> extractCommTableStatement2() throws InterruptedException {		
		for (WebElement tr : commTableHistory) {
			if (tr.isDisplayed()) {
				List<WebElement> tdData = tr.findElements(By.xpath(".//td | .//th"));
				ArrayList<String> tdRowData = new ArrayList<>();
				for (WebElement td : tdData) {
					tdRowData.add(td.getText());
				}
				ACTIVITY_STATEMENT_DATA2.add(tdRowData);
			}
		}
		System.out.println(ACTIVITY_STATEMENT_DATA2);
		ClientExcel.writeDataToExcel2(ACTIVITY_STATEMENT_DATA2);
		return ACTIVITY_STATEMENT_DATA2;
	}

	public void clickAllLinks() throws InterruptedException {
		try {
		wait.until(ExpectedConditions.elementToBeClickable(next2));
		next2.click();
		extractCommTableStatement2();
		}
		catch(Exception e) {
			extractCommTableStatement();
		}
		Thread.sleep(5000);
	
		for (int i = 0; i < links.size(); i++) {
			try {
				WebElement link = links.get(i);
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", link);
				wait.until(ExpectedConditions.visibilityOf(link));
				wait.until(ExpectedConditions.elementToBeClickable(link));
				Actions actions = new Actions(driver);
				actions.moveToElement(link).pause(500).click().perform();
				waitForDownloadCompletion();
//				extractCommTableStatement();
				printLatestDownloadedFileName2(downloadDir);
			} catch (Exception e) {
				//System.err.println("Actions click failed for: " + links.get(i).getText());
				//fallbackToJavaScriptClick(links.get(i));
			}
			pause(3000);
		}
	}

	public void clickNextButton() throws InterruptedException{
		try {
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", next);
			wait.until(ExpectedConditions.visibilityOf(next));
			wait.until(ExpectedConditions.elementToBeClickable(next));
			Thread.sleep(5000);
			Actions actions = new Actions(driver);
			actions.moveToElement(next).pause(500).click().perform();
			Thread.sleep(3000);	
			//			wait.until(ExpectedConditions.elementToBeClickable(next2));
			//			next2.click();
			//			actions.moveToElement(next).pause(500).click().perform();		
			try {
				Thread.sleep(2000);
				wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//table//tr//td[position()=2]//a"), 0));
				

				List<WebElement> newLinks = driver.findElements(By.xpath("//table//tr//td[position()=2]//a"));
				extractCommTableStatement();
				for (int i = 0; i < newLinks.size(); i++) {
					try {
						WebElement link = newLinks.get(i);
						((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", link);
						wait.until(ExpectedConditions.visibilityOf(link));
						wait.until(ExpectedConditions.elementToBeClickable(link));
						Actions actions1 = new Actions(driver);
						actions1.moveToElement(link).pause(500).click().perform();
						waitForDownloadCompletion();
						printLatestDownloadedFileName(downloadDir);
//						extractCommTableStatement();
//						clickAllLinks();
						
					} catch (Exception e) {
						fallbackToJavaScriptClick(newLinks.get(i));
					}
					pause(3000);
				}
			}
			catch(Exception e1) {
//				clickAllLinks();
//				extractCommTableStatement();
			}
		}
		catch (Exception e) {
//			clickAllLinks();
//			extractCommTableStatement();
			//			fallbackToJavaScriptClick(next);
		}
		pause(3000);
	}
	public void printLatestDownloadedFileName2(String downloadDir) throws InterruptedException {
		Thread.sleep(3000);
		File dir = new File(downloadDir);
		File[] files = dir.listFiles();

		if (files != null && files.length > 0) {
			File latestFile = files[0];
			for (File file : files) {
				if (file.lastModified() > latestFile.lastModified()) {
					latestFile = file;
				}
			}
			name = latestFile.getName();
			ClientExcel.addPdfName(name);
		} else {
		}
	}
	private void fallbackToJavaScriptClick(WebElement element) {
		try {
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
			waitForDownloadCompletion();
			printLatestDownloadedFileName(downloadDir);
		} catch (Exception e) {
		}
	}
	private void waitForDownloadCompletion() throws InterruptedException {
		Thread.sleep(3000);
	}
	private void pause(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	public void printLatestDownloadedFileName(String downloadDir) throws InterruptedException {
		Thread.sleep(3000);
		File dir = new File(downloadDir);
		File[] files = dir.listFiles();
		if (files != null && files.length > 0) {
			File latestFile = files[0];
			for (File file : files) {
				if (file.lastModified() > latestFile.lastModified()) {
					latestFile = file;
				}
			}
			name = latestFile.getName();
			ClientExcel.addPdfName(name);
		}
	}
	public void closeBrowser() {
		DriverManager.getDriver().quit();
	}
}