package Pages;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.asis.util.ClientExcel;
import com.asis.util.MainClass;

import Driver_manager.DriverManager;

public class TaxReturnPage extends MainClass {
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
	//span[contains(text(),'Cancel')]
	//td[contains(@class,'x-grid-cell')]//i[@title='Accepted']/ancestor::a
 
	@FindBy(xpath="//label[text()='Date of issue']/following::input[1]")
	private static WebElement dateOfIssue;
	@FindBy(xpath="//label[text()='ATO Reference']/following::input[1]")
	private static WebElement atoRef;
	@FindBy(xpath="//label[text()='Taxable Income']/following::input[1]")
	private static WebElement taxableIncome;
	@FindBy(xpath="//input[@name='PayableRefundable']")
	private static WebElement payableRefundable;

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

		// Check if the PDF file exists
		if (pdfFile.exists()) {
//			System.out.println("Found PDF: " + pdfFileName);
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

//
//	public static void processAllNoticesOfAssessment(String filePath, String downloadDir) {
//		ClientExcel.clientNamesRemoval();
////		System.out.println("client name in tax method before " + clientNames.size());
//		subjectColumnData = ClientExcel.readSubjectColumn(filePath);
//		boolean found = false; 
//
//		for (int i = 0; i < subjectColumnData.size(); i++) {
//			String subject = subjectColumnData.get(i).trim();
//
//			if (subject.toLowerCase().startsWith("notice of assessment")) {
//				found = true; 
//
//				String clientName = clientNames.get(i).trim();
////				System.out.println("Processing Client: " + clientName);
//
//				clickTaxButton();
//				clickReturnsButton();
//				clickFilledButton();
//				clickSearchButton(clientName);
//				try {
//					clickAddButton();
//					String pdfFileName = ClientExcel.readPdfFileNamesFromColumn8(filePath).get(i).trim();
//					searchAndExtractPdfData(filePath,downloadDir,pdfFileName);
//					Thread.sleep(10000);
//					clickCancelButton();
//					
//				}
//				catch(Exception e) {
//					String pdfFileName = ClientExcel.readPdfFileNamesFromColumn8(filePath).get(i + 1).trim();
//
//				}
//			}
//		}
//		if (!found) {
////			System.out.println("No 'Notice of Assessment' found in the subject column.");
//		}
////		System.out.println("client name in tax method after " + clientNames.size());
////		clientNames.clear();
//	}
	
	
	
	public static void processAllNoticesOfAssessment(String filePath, String downloadDir) {
	    ClientExcel.clientNamesRemoval();
//		System.out.println("client name in tax method before " + clientNames.size());
	    subjectColumnData = ClientExcel.readSubjectColumn(filePath);
	    boolean found = false; 

	    for (int i = 0; i < subjectColumnData.size(); i++) {
	        String subject = subjectColumnData.get(i).trim();

	        if (subject.toLowerCase().startsWith("notice of assessment")) {
	            found = true; 

	            String clientName = clientNames.get(i).trim();
//				System.out.println("Processing Client: " + clientName);
	            clickTaxButton();
	            clickReturnsButton();
	            clickFilledButton();
	            clickSearchButton(clientName);
	            try {
	                clickAddButton();
	                String pdfFileName = ClientExcel.readPdfFileNamesFromColumn8(filePath).get(i).trim();
	                searchAndExtractPdfData(filePath, downloadDir, pdfFileName);
	                Thread.sleep(10000);
	                clickCancelButton();
	            } catch (Exception e) {
	                
	            }
	        }
	    }
	    if (!found) {
	        // System.out.println("No 'Notice of Assessment' found in the subject column.");
	    }
	}


	
	public static HashMap<String, String> readPdfFile(String pdfFilePath) {
		File pdfFile = new File(pdfFilePath);
		HashMap<String, String> extractedData = new HashMap<>();

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

//				System.out.println("Extracted Data: " + extractedData);
			} else {
//				System.out.println("The PDF is encrypted. Cannot read.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return extractedData;
	}

}
