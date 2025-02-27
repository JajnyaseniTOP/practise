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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ATOcommHistoryExtarctionPage extends MainClass {
	ClientExcel creator = new ClientExcel();


	@FindBy(xpath = "//button[@title='Next page']")
	private WebElement next;

	@FindBy(xpath = "//button[@title='Download']")
	private WebElement download;

	@FindBy(xpath = "//button[contains(text(),'Yes')]")
	private WebElement yesPopUp;

	@FindBy(xpath = "//table//tr//td[position()=2]//a")
	private List<WebElement> links; 
	
	//table//tr//td[position()=2]//a
	
	@FindBy(xpath = "//tbody/tr[@class=\"table-row\"]")
	private List<WebElement> commTableHistory;
	@FindBy(xpath = "//th[@data-header='Name']")
	private List<WebElement> clientName;
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

	public void clickAllLinks() {
	    for (int i = 0; i < links.size(); i++) {
	        try {
	            WebElement link = links.get(i); // Fetch the current link dynamically

	            // Scroll into view to ensure visibility
	            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", link);

	            // Explicit wait for visibility and clickability
	            wait.until(ExpectedConditions.visibilityOf(link));
	            wait.until(ExpectedConditions.elementToBeClickable(link));

	            // Try clicking using Actions
	            Actions actions = new Actions(driver);
	            actions.moveToElement(link).pause(500).click().perform();
	            System.out.println("Clicked using Actions: " + link.getText());

	            // Wait for download completion and print file name
	            waitForDownloadCompletion();
	       
	            printLatestDownloadedFileName(downloadDir);

	        } catch (Exception e) {
	            System.err.println("Actions click failed for: " + links.get(i).getText());
	            fallbackToJavaScriptClick(links.get(i));
	        }

	        // Small pause to ensure all actions complete before moving to the next link
	        pause(3000);
	    }
	}

	// Fallback to JavaScript click
	private void fallbackToJavaScriptClick(WebElement element) {
	    try {
	        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
	        System.out.println("Clicked using JavaScript: " + element.getText());

	        // Wait for download completion and print file name
	        waitForDownloadCompletion();
	        printLatestDownloadedFileName(downloadDir);

	    } catch (Exception e) {
	        System.err.println("JavaScript click also failed for: " + element.getText() + " Error: " + e.getMessage());
	    }
	}

	// Custom wait method for download completion
	private void waitForDownloadCompletion() throws InterruptedException {
	    Thread.sleep(3000); // Simulate file download wait; adjust timing or use proper checks
	}

	// Pause method for reusability
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
		} else {
		}
	}
	public void closeBrowser() {
		DriverManager.getDriver().quit();
	}
}
