package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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

	@FindBy(xpath = "//tbody[@data-bind=\"css: { 'rowgroup': rowGroup.header }\"]//tr//td//a")
	private List<WebElement> links;  
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
//		System.out.println(ACTIVITY_STATEMENT_DATA);
		ClientExcel.writeDataToExcel(ACTIVITY_STATEMENT_DATA);
		return ACTIVITY_STATEMENT_DATA;
	}

	public void clickAllLinks() throws InterruptedException {
//		String downloadDir = "C:\\Users\\test\\Downloads";

		wait.until(ExpectedConditions.visibilityOfAllElements(links));

		for (WebElement link : links) {
			wait.until(ExpectedConditions.elementToBeClickable(link));
			Thread.sleep(3000);

			link.click(); 
			Thread.sleep(5000);
			printLatestDownloadedFileName(downloadDir);
		}
	}
	public void printLatestDownloadedFileName(String downloadDir) {
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
			//			ClientExcel.saveExcelFile();
//			System.out.println("Downloaded file saved as: " + name);
		} else {
//			System.out.println("No files found in the directory.");
		}
	}
	public void closeBrowser() {
		DriverManager.getDriver().quit();
	}
}
