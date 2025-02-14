package Pages;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.asis.util.ClientExcel;
import com.asis.util.MainClass;

import Driver_manager.DriverManager;

public class TaxReturnPage extends MainClass {
	public static String clientIds;
	public static String cellText;
	public static HashMap<String, String> extractedData = new HashMap<>();

	@FindBy(xpath="//button[contains(text(),'Tax')]")
	private static WebElement tax;
	@FindBy(xpath="//a[contains(text(),'Returns')]")
	private static WebElement returns;
	@FindBy(xpath="//span[text()='Filed']")
	private static WebElement filled;
	@FindBy(xpath="//input[@id='text-1016-inputEl']")
	private static WebElement search;
	@FindBy(xpath="//td//a[contains(@class, 'noaLink')]//i[@title='Add']")
	private static WebElement add;
	@FindBy(xpath="//span[contains(text(),'Cancel')]")
	private static WebElement cancel;
	@FindBy(xpath="//label[text()='Date of issue']/following::input[1]")
	private static WebElement dateOfIssue;
	@FindBy(xpath="//label[text()='ATO Reference']/following::input[1]")
	private static WebElement atoRef;
	@FindBy(xpath="//label[text()='Taxable Income']/following::input[1]")
	private static WebElement taxableIncome;
	@FindBy(xpath="//input[@name='PayableRefundable']")
	private static WebElement payableRefundable;

	@FindBy(xpath="//span[contains(text(),'Fortuna Unit Trust t/as Keypoi…')]")
	private static WebElement switchPortal_keypoint;
	@FindBy(xpath="//span[contains(text(),'Fortuna Accountants & Business…')]")
	private static WebElement switchPortal_business;
	@FindBy(xpath="//div[@class='xnav-appbutton--body']")
	private static WebElement switchPortal;
	@FindBy(xpath="//a[normalize-space()='Portal']")
	private static WebElement clickPortal;
	@FindBy(xpath = "//input[@value='Connect']")
	private static WebElement clickConnect;
	@FindBy(xpath = "//tbody[@id='gridview-1050-body']")
	private static WebElement tabel;

	//span[contains(text(),'Period End')]

	public static  void switchportal() throws InterruptedException {
		Thread.sleep(3000);
		try {
			switchPortal_keypoint.click();
			clickPortal.click();
			clickConnect.click();
		}catch(Exception e) {
			//System.out.println("catch block in switchPortal_keypoint");
		}
	}
	public static  void switchportal2() throws InterruptedException {
		Thread.sleep(3000);
		try {
			switchPortal_business.click();
			clickPortal.click();
			clickConnect.click();
		}catch(Exception e) {
			//System.out.println("catch bloclk switchPortal_business");
		}
	}

	public TaxReturnPage(){
		PageFactory.initElements(DriverManager.getDriver(), this); 
	}

	public static void clickTaxButton() {
		wait.until(ExpectedConditions.elementToBeClickable(tax));
		tax.click();
	}
	public static void clickAddButton() {
		wait.until(ExpectedConditions.elementToBeClickable(add));
		add.click();
	}
	public static void clickCancelButton() {
		wait.until(ExpectedConditions.elementToBeClickable(cancel));
		cancel.click();
	}
	public static void clickReturnsButton() {
		wait.until(ExpectedConditions.elementToBeClickable(returns));
		returns.click();
	}

	public static void clickFilledButton() {
		wait.until(ExpectedConditions.elementToBeClickable(filled));
		filled.click();
	}

	public static void clickSearchButton(String clientName) {
		wait.until(ExpectedConditions.elementToBeClickable(search));
		search.click();
		search.sendKeys(clientName);
		search.sendKeys(Keys.ENTER);
	}

	public static void clickDateButton(String dateOfIssue1) {
		wait.until(ExpectedConditions.elementToBeClickable(dateOfIssue));
		dateOfIssue.clear();
		dateOfIssue.sendKeys(dateOfIssue1);
	}

	public static void clickAtoRefButton(String referenceNumber) {
		wait.until(ExpectedConditions.elementToBeClickable(atoRef));
		atoRef.clear();
		atoRef.sendKeys(referenceNumber);
	}

	public static void clickTaxableIncomeButton(String taxableIncome1) {
		wait.until(ExpectedConditions.elementToBeClickable(taxableIncome));
		taxableIncome.clear();
		taxableIncome.sendKeys(taxableIncome1);
	}

	public static void clickPayableRefundableButton(String resultAmount) {
		wait.until(ExpectedConditions.elementToBeClickable(payableRefundable));
		payableRefundable.clear();
		payableRefundable.sendKeys(resultAmount);
	}
	public static void searchAndExtractPdfData(String filePath, String downloadDir, String pdfFileName) throws InterruptedException {
		String fullPath = downloadDir + File.separator + pdfFileName;
		File pdfFile = new File(fullPath);
		if (pdfFile.exists()) {
			HashMap<String, String> extractedData = readPdfFile(fullPath);

			if (extractedData != null && !extractedData.isEmpty()) {
				clickDateButton(extractedData.get("Date of Issue"));
				Thread.sleep(1000);
				clickAtoRefButton(extractedData.get("Reference Number"));
				Thread.sleep(1000);
				clickTaxableIncomeButton(extractedData.get("Taxable Income"));
				Thread.sleep(1000);
				clickPayableRefundableButton(extractedData.get("Result"));
			}
		} else {
			//			System.out.println("PDF Not Found: " + pdfFileName);
		}
	}
	public static void accessTableRowsAndColumns() {
		wait.until(ExpectedConditions.visibilityOf(tabel));

		List<WebElement> rows = tabel.findElements(By.xpath(".//tr"));        
		for (WebElement row : rows) {
			List<WebElement> cells = row.findElements(By.xpath(".//td"));            
			//            if (cells.size() == 6) {
			for (int i = 0; i < cells.size(); i++) {
				cellText = cells.get(6).getText();
				//                    System.out.println("Column " + (i + 1) + ": " + cellText);        
				System.out.println("Column " + cellText); 
				//                }
			}
			//                else {
			//                System.out.println("Row does not have 6 columns.");
			//            }

		}
	}
	public static void processAllNoticesOfAssessment(String filePath, String downloadDir) throws InterruptedException{
		ClientExcel.clientNamesRemoval();
		ArrayList<String> client_ID =ClientExcel.readSecondColumn(filePath);
		//		System.out.println("client name in tax method before " + clientNames.size());
		subjectColumnData = ClientExcel.readSubjectColumn(filePath);
		boolean found = false; 
		String clientName;
		String subject;
		for (int i = 0; i < subjectColumnData.size(); i++) {
			subject = subjectColumnData.get(i).trim();
			//found = true; 

			if (subject.toLowerCase().startsWith("notice of assessment")) {
				found = true; 
				switchportal2();
				clientName = clientNames.get(i).trim();
				clientIds =client_ID.get(i);
				clickTaxButton();
				clickReturnsButton();
				clickFilledButton();
				clickSearchButton(clientIds);
				try {
					if(extractedData.get("Year").equals(cellText)) {
						clickAddButton();
						Thread.sleep(10000);
						String pdfFileName = ClientExcel.readPdfFileNamesFromColumn8(filePath).get(i).trim();
						searchAndExtractPdfData(filePath, downloadDir, pdfFileName);
						clickCancelButton();
					}			
					

				} catch (Exception e) {
					switchportal();

					clickTaxButton();
					clickReturnsButton();
					clickFilledButton();
					clickSearchButton(clientIds);
					try {
						if(extractedData.get("Year").equals(cellText)) {
							clickAddButton();
							Thread.sleep(10000);
							String pdfFileName = ClientExcel.readPdfFileNamesFromColumn8(filePath).get(i).trim();
							searchAndExtractPdfData(filePath, downloadDir, pdfFileName);
							clickCancelButton();
						}
						
					} catch (Exception e1) {

					}

				}
			}
			if (!found) {
				// System.out.println("No 'Notice of Assessment' found in the subject column.");

			}
		}
	}

	public static HashMap<String, String> readPdfFile(String pdfFilePath) {
		File pdfFile = new File(pdfFilePath);


		if (pdfFilePath.toLowerCase().endsWith(".html")) {
			//			System.out.println("Found HTML file. Skipping: " + pdfFilePath);
			return extractedData;
		}

		try (PDDocument document = PDDocument.load(pdfFile)) {
			if (!document.isEncrypted()) {
				PDFTextStripper pdfStripper = new PDFTextStripper();
				String pdfText = pdfStripper.getText(document);

				Pattern datePattern = Pattern.compile("Date of issue\\s*(\\d{2} \\w+ \\d{4})");
				Matcher dateMatcher = datePattern.matcher(pdfText);
				if (dateMatcher.find()) {
					String dateOfIssue = dateMatcher.group(1);
					extractedData.put("Date of Issue", dateOfIssue);
				}
				else {
					extractedData.put("Date of Issue", "0.0");
				}

				Pattern refPattern = Pattern.compile("Our reference\\s*(\\d{3} \\d{3} \\d{3} \\d{4})");
				Matcher refMatcher = refPattern.matcher(pdfText);
				if (refMatcher.find()) {
					String referenceNumber = refMatcher.group(1);
					extractedData.put("Reference Number", referenceNumber);
				}
				else {
					extractedData.put("Reference Number", "0.0");
				}

				Pattern incomePattern = Pattern.compile("Your taxable income is \\$([\\d,]+)");
				Matcher incomeMatcher = incomePattern.matcher(pdfText);
				if (incomeMatcher.find()) {
					String taxableIncome = incomeMatcher.group(1).replace(",", "");
					extractedData.put("Taxable Income", taxableIncome);
				}
				else {
					extractedData.put("Taxable Income", "0.0");
				}

				Pattern resultPattern = Pattern.compile("Result of this notice\\s+(\\S+ \\S+)");
				Matcher resultMatcher = resultPattern.matcher(pdfText);
				if (resultMatcher.find()) {
					String resultAmount = resultMatcher.group(1);
					extractedData.put("Result", resultAmount);
				} else {
					extractedData.put("Result", "0.0");
				}

				Pattern yearPattern = Pattern.compile("Notice of assessment - year ended\\s*(\\d{2} \\w+ \\d{4})");
				Matcher yearMatcher = yearPattern.matcher(pdfText);
				if (yearMatcher.find()) {
					String yearAmount = yearMatcher.group(1);
					extractedData.put("Year", yearAmount);
					System.out.println("Year: " + yearAmount);
				} else {
					extractedData.put("Year", "0.0");  
				}
			} else {
				System.out.println("The PDF is encrypted. Cannot read.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return extractedData;
	}

}