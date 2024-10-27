package Pages;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.asis.util.ClientExcel;
import com.asis.util.MainClass;

import Driver_manager.DriverManager;



public class XeroSearchClientPage extends MainClass {
	public static String client;
	public static String subject;
	public String emailText = null;
	public String clientCodeText = null;

	@FindBy(xpath = "//button[@title='GlobalSearch']//div[@role='presentation']//*[name()='svg']")
	WebElement searchButton;

	@FindBy(xpath = "//input[@placeholder='Search']")
	WebElement inputBox;

	@FindBy(xpath = "//div[@class='form-item']//div[contains(text(), 'Client Code')]/following-sibling::div")
	WebElement clientCode;

	@FindBy(xpath = "//div[@class='panel-item']//span[contains(text(), 'Email')]/following-sibling::span/a")
	WebElement clientEmail;

	@FindBy(xpath = "//span[@class='value u-email']")
	WebElement clientEmail2;

	public XeroSearchClientPage() {
		PageFactory.initElements(DriverManager.getDriver(), this);
	}

	public void clickOnSearchButton() {
		searchButton.click();
	}

	public void inputTheClientName() throws InterruptedException {
		System.out.println("client names " + clientNames.size());
		ClientExcel.readSubjectColumn(filePath);
		System.out.println("client names " + clientNames.size());
		System.out.println("subject data " + subjectColumnData.size());

		for (int i = 0; i < clientNames.size(); i++) {
			client = clientNames.get(i);
			subject = subjectColumnData.get(i);
			Thread.sleep(3000);

			// if search button will work
			try {
				inputBox.clear();
				inputBox.sendKeys(client);
				Thread.sleep(3000);
			}
			// if search button will not work
			catch(Exception e) {
				clickOnSearchButton();
				inputBox.clear();
				inputBox.sendKeys(client);
				Thread.sleep(3000);
			}

			// try to find the client if client name visible click 
			try {
				List<WebElement> elements = DriverManager.getDriver().findElements(By.xpath("//a"));
				boolean clientFound = false;

				for (WebElement ele : elements) {
					if (ele.getText().trim().equalsIgnoreCase(client.trim())) {
						ele.click();
						clientFound = true;
						break;
					}
				}

				if (!clientFound && client.contains(".")) {
					String clientWithoutDot = client.replace(".", "").trim();

					// if client have . in their name 
					try {
						inputBox.clear();
						inputBox.sendKeys(clientWithoutDot);
						Thread.sleep(3000);
						elements = DriverManager.getDriver().findElements(By.xpath("//a"));
						for (WebElement ele : elements) {
							if (ele.getText().trim().equalsIgnoreCase(clientWithoutDot)) {
								ele.click();
								clientFound = true;
								break;
							}
						}
					}
					// if client dont have . in their name 
					catch(Exception e) {
						clickOnSearchButton();
					}
				}
				//client found
				if (clientFound) {
					//extract email of that client using i xpath
					try {
						Thread.sleep(4000);
						wait.until(ExpectedConditions.visibilityOf(clientEmail));
						emailText = clientEmail.getText().trim();

					} 
					// if not found use 2 xpath for email
					catch (Exception e1) {
						try {
							Thread.sleep(4000);
							wait.until(ExpectedConditions.visibilityOf(clientEmail2));
							emailText = clientEmail2.getText().trim();

						} catch (Exception e2) {
							System.out.println("Client email is not there.");
						}
					}

					// check if client code is visible 
					try {
						wait.until(ExpectedConditions.visibilityOf(clientCode));
						if (clientCode.isDisplayed()) {
							clientCodeText = clientCode.getText().trim();
						}
					}
					// if client code is not visible
					catch (Exception e) {
						System.out.println("Client code is not there.");
					}	

					if (emailText != null && clientCodeText != "-") {
						ClientExcel.addClientData(clientCodeText, emailText);
						ClientExcel.writeCombinedDataToExcel(clientCodeText, subject);
						clickOnSearchButton();
					} else {
						ClientExcel.addClientData("client code not found", "client email not found");
						ClientExcel.writeCombinedDataToExcel(clientCodeText, subject);
						ClientExcel.saveExcelFile();
						clickOnSearchButton();
					}
				} else {
					Thread.sleep(3000);
					ClientExcel.addClientData("client name not found", "client name not found");
					ClientExcel.writeCombinedDataToExcel(clientCodeText, subject);
					ClientExcel.saveExcelFile();
				}

			} 
			// if client name is not visible on the sear directory
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void renameAndMovePdfFilesToDownloadsFolder(String downloadDir) {
		ArrayList<String> pdfFileNames = ClientExcel.readPdfFilePathFromColumn8(filePath);
		ArrayList<String> fileNamesColumn7 = ClientExcel.readFileNamesFromColumn7(filePath);

		if (pdfFileNames.size() != fileNamesColumn7.size()) {
			System.out.println("Mismatch between column 8 and column 7 sizes.");
			return;
		}

		File downloadsFolder = new File(downloadDir + File.separator + "Downloads");
		if (!downloadsFolder.exists()) {
			boolean created = downloadsFolder.mkdir();
			if (created) {
				System.out.println("Downloads folder created.");
			} else {
				System.out.println("Failed to create Downloads folder.");
				return;
			}
		}

		int cnt = 0;
		for (String pdfFileName : pdfFileNames) {
			String fullPath = downloadDir + File.separator + pdfFileName.trim();
			File pdfFile = new File(fullPath);

			if (pdfFile.exists()) {
				System.out.println("Found: " + pdfFileName);

				String currentExtension = getFileExtension(pdfFile);

				if (cnt < fileNamesColumn7.size()) {
					String newFileName = fileNamesColumn7.get(cnt) + "." + currentExtension;
					String newFilePath = downloadDir + File.separator + newFileName;
					File renamedFile = new File(newFilePath);

					int fileCount = 1; 
					while (renamedFile.exists()) {
						newFileName = "new_" + fileNamesColumn7.get(cnt) + "_" + fileCount + "." + currentExtension;
						renamedFile = new File(downloadDir + File.separator + newFileName);
						fileCount++;
					}

					System.out.println("Renaming file to: " + newFileName);

					if (pdfFile.renameTo(renamedFile)) {
						System.out.println("Renamed " + pdfFileName + " to " + newFileName);

						File targetFile = new File(downloadsFolder + File.separator + newFileName);
						try {
							Files.move(renamedFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
							System.out.println("Moved " + newFileName + " to Downloads folder.");
						} catch (IOException e) {
							System.out.println("Failed to move " + newFileName + " to Downloads folder.");
							e.printStackTrace();
						}
					} else {
						System.out.println("Failed to rename " + pdfFileName);
					}
					cnt++;
				} else {
					System.out.println("Index out of bounds for fileNamesColumn7.");
					break;
				}
			} else {
				System.out.println("File not found: " + pdfFileName);
			}
		}
	}

	private String getFileExtension(File file) {
		String fileName = file.getName();
		int dotIndex = fileName.lastIndexOf('.');
		if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
			return fileName.substring(dotIndex + 1);
		} else {
			return "";
		}
	}
}
