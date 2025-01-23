package Pages;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.asis.util.ClientExcel;
import com.asis.util.MainClass;

import Driver_manager.DriverManager;



public class XeroSearchClientPageJJ extends MainClass {
	public static String client;
	public static String subject;
	public String emailText = null;
	public String clientCodeText = null;
	public String internal_team=null;

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
	
	@FindBy(xpath = "//div[contains(@class, 'form-item') and .//div[text()='Internal Team']]//div[@class='value']/span")
	WebElement internalTeam;
	
	@FindBy(xpath="//span[contains(text(),'Fortuna Unit Trust t/as Keypoi…')]")
	WebElement switchPortal_keypoint;
	
	@FindBy(xpath="//span[contains(text(),'Fortuna Accountants & Business…')]")
	WebElement switchPortal_business;
	
	@FindBy(xpath="//a[normalize-space()='Portal']")
	WebElement clickPortal;
	
	@FindBy(xpath = "//input[@value='Connect']")
	WebElement clickConnect;
	
	
	public XeroSearchClientPageJJ() {
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

	        // If search button will work
	        try {
	        	
	            //clickOnSearchButton();
	            inputBox.clear();
	            inputBox.sendKeys(client);
	            Thread.sleep(3000);
	        }
	        // If search button will not work
	        catch (Exception e) {
	            clickOnSearchButton();
	            inputBox.clear();
	            inputBox.sendKeys(client);
	            Thread.sleep(3000);
	        }

	        boolean clientFound = false;

	        // Try to find the client if client name is visible, click
	        try {
	            List<WebElement> elements = DriverManager.getDriver().findElements(By.xpath("//a"));

	            for (WebElement ele : elements) {
	                String elementText = ele.getText();
	                String editedWebElementText = elementText.toLowerCase().trim();
//	                System.out.println("Raw element text: '" + elementText);
//	                System.out.println("Processed element text: '" + editedWebElementText);
//	                System.out.println("Client: '" + client.toLowerCase().trim());

	                if (elementText.trim().equalsIgnoreCase(client.trim())) {
	                    System.out.println("Match found in equalsIgnoreCase condition.");
	                    Thread.sleep(2000);
	                    ele.click();
	                    clientFound = true;
	                    break;
	                } else if (editedWebElementText.contains(client.toLowerCase().trim())) {
	                    System.out.println("Match found in contains condition.");
	                    Thread.sleep(2000);
	                    ele.click();
	                    clientFound = true;
	                    break;
	                } else if (editedWebElementText.contains(" & ")) {
	                    String removeAndFromText = editedWebElementText.replaceAll("\\s*&\\s*", "&");
	                    if (removeAndFromText.contains(client.toLowerCase().trim())) {
	                        Thread.sleep(2000);
	                        ele.click();
	                        clientFound = true;
	                        break;
	                    }
	                }
	            }

	            // If client not found and contains a dot
	            if (!clientFound && client.contains(".")) {
	                clientFound = searchClientWithoutDot(client);
	            }

	            // If client is found, extract details
	            if (clientFound) {
	                extractClientDetails();
	            } else {
	                // Call handleClientNotFound after both portal searches
	                searchInSecondaryPortal();
	                if (!clientFound) {
	                    handleClientNotFound(subject);  // Handle after both searches
	                }
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}

	/**
	 * Searches for a client without dots in their name.
	 *
	 * @param clientName The client name with dots.
	 * @return true if the client was found, false otherwise.
	 * @throws InterruptedException
	 */
	private boolean searchClientWithoutDot(String clientName) throws InterruptedException {
	    boolean clientFound = false;
	    String clientWithoutDot = clientName.replace(".", "").trim();
	    inputBox.clear();
	    inputBox.sendKeys(clientWithoutDot);
	    Thread.sleep(3000);

	    List<WebElement> elements = DriverManager.getDriver().findElements(By.xpath("//a"));
	    for (WebElement ele : elements) {
	        String elementText = ele.getText().trim();
	        if (elementText.equalsIgnoreCase(clientWithoutDot) || elementText.toLowerCase().contains(clientWithoutDot.toLowerCase())) {
	        	Thread.sleep(2000);
	            ele.click();
	            clientFound = true;
	            break;
	        }
	    }
	    return clientFound;
	}

	/**
	 * Switches to the secondary portal and applies the same search logic.
	 *
	 * @throws InterruptedException
	 */
	private void searchInSecondaryPortal() throws InterruptedException {
	    try {
//	    	switchPortal_keypoint.click();
//	        clickPortal.click();
//	        Thread.sleep(2000);
//	        if(clickConnect_BusinessPortal.isDisplayed()) {
//	        	clickConnect_BusinessPortal.click();
//	        }else {
//	        	clickConnect.click();
//	        }
	        
	        Thread.sleep(5000);

	        // Reuse the primary search logic for the secondary portal
	        clickOnSearchButton();
	        inputBox.clear();
	        inputBox.sendKeys(client);
	        Thread.sleep(3000);

	        boolean clientFound = false;
	        List<WebElement> elements = DriverManager.getDriver().findElements(By.xpath("//a"));

	        for (WebElement ele : elements) {
	            String elementText = ele.getText().trim();
	            String editedWebElementText = elementText.toLowerCase().trim();
	            if (elementText.equalsIgnoreCase(client.trim()) || elementText.toLowerCase().contains(client.toLowerCase().trim())) {
	            	Thread.sleep(2000);
	                ele.click();
	                clientFound = true;
	                break;
	            }
	            else if (editedWebElementText.contains(" & ")) {
                    String removeAndFromText = editedWebElementText.replaceAll("\\s*&\\s*", "&");
                    if (removeAndFromText.contains(client.toLowerCase().trim())) {
                        Thread.sleep(2000);
                        ele.click();
                        clientFound = true;
                        break;
                    }
                }
	        }

	        // If client not found and contains a dot, search without the dot
	        if (!clientFound && client.contains(".")) {
	            clientFound = searchClientWithoutDot(client);
	        }

	        if (clientFound) {
	            extractClientDetails();
	        }

	    } catch (Exception e) {
	        System.out.println("Error switching to the secondary portal.");
	        e.printStackTrace();
	    }
	}

	/**
	 * Extracts client details like email, client code, and internal team.
	 */
	private void extractClientDetails() {
	    try {
	        Thread.sleep(4000);
	        wait.until(ExpectedConditions.visibilityOf(clientEmail));
	        emailText = clientEmail.getText().trim();
	    } catch (Exception e1) {
	        try {
	            Thread.sleep(4000);
	            wait.until(ExpectedConditions.visibilityOf(clientEmail2));
	            emailText = clientEmail2.getText().trim();
	        } catch (Exception e2) {
	            emailText = "no email found";
	            System.out.println("Client email is not there.");
	        }
	    }

	    try {
	        wait.until(ExpectedConditions.visibilityOf(clientCode));
	        if (clientCode.isDisplayed()) {
	            clientCodeText = clientCode.getText().trim();
	        }
	    } catch (Exception e) {
	        clientCodeText = "no client code";
	        System.out.println("Client code is not there.");
	    }

	    try {
	        wait.until(ExpectedConditions.visibilityOf(internalTeam));
	        if (internalTeam.isDisplayed()) {
	            internal_team = internalTeam.getText().trim();
	        }
	    } catch (Exception e) {
	        internal_team = "no teamName";
	        System.out.println("Internal team is not there.");
	    }

	    ClientExcel.addClientData(clientCodeText, emailText, internal_team);
	    ClientExcel.writeCombinedDataToExcel(clientCodeText, subject);
	    clickOnSearchButton();
	}

	/**
	 * Handles cases where a client is not found.
	 *
	 * @param subject The subject associated with the client.
	 */
	private void handleClientNotFound(String subject) throws InterruptedException {
	    Thread.sleep(3000);
	    ClientExcel.addClientData("client name not found", "client name not found", "no teamName");
	    ClientExcel.writeCombinedDataToExcel("null", subject);
	    ClientExcel.saveExcelFile();
	}

	
	

	
	
	
	private String normalizeText(String text) {
		if (text == null) return "";
	    return text.replace("\u00A0", " ")       // Replace non-breaking spaces
	               .replaceAll("&amp;", "&")     // Replace encoded ampersand
	               .replaceAll("\\s+", " ")      // Normalize multiple spaces
	               .replaceAll("[^\\p{Print}]", "") // Remove non-printable characters
	               .trim()                       // Trim leading/trailing spaces
	               .toLowerCase(); 
		
	}

	public void renameAndMovePdfFilesToDownloadsFolder(String downloadDir){
	    ArrayList<String> pdfFileNames = ClientExcel.readPdfFileNamesFromColumn8(filePath);
	    ArrayList<String> fileNamesColumn7 = ClientExcel.readFileNamesFromColumn7(filePath);
	    ArrayList<String> teamNames = ClientExcel.readTeamNamesFromColumn9(filePath);  // Read team names from column 9
 
	    if (pdfFileNames.size() != fileNamesColumn7.size() || pdfFileNames.size() != teamNames.size()) {
	        System.out.println("Mismatch between the file lists or team names.");
	        return;
	    }
 
	    File downloadsFolder = new File(downloadDir + File.separator + "Email_Files_" + currentDate);
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
 
	            if (cnt < fileNamesColumn7.size()){
	                String newFileName = fileNamesColumn7.get(cnt) + "." + currentExtension;
	                String newFilePath = downloadDir + File.separator + newFileName;
	                File renamedFile = new File(newFilePath);  //at this line the file is renaming
	                
	                int fileCount = 1;
	                while (renamedFile.exists()) {
	                    newFileName = "new_" + fileNamesColumn7.get(cnt) + "_" + fileCount + "." + currentExtension;
	                    renamedFile = new File(downloadDir + File.separator + newFileName);
	                    fileCount++;
	                }
 
	                System.out.println("Renaming file to: " + newFileName);
//----------------------------------------------------------------------------------------------------------------
	                if (pdfFile.renameTo(renamedFile)) {
	                    System.out.println("Renamed " + pdfFileName + " to " + newFileName);
 
	                    // Determine target folder based on team name
	                    String teamName = teamNames.get(cnt).trim();
	                    File targetFolder;
	                    switch (teamName) {
	                    case "K":
	                        targetFolder = new File(downloadDir + File.separator + "Email_Files_" + currentDate + File.separator + "K-Lindy");
	                        break;
	                    case "C":
	                    case "C1":
	                        targetFolder = new File(downloadDir + File.separator + "Email_Files_" + currentDate + File.separator + "C-Rebecca");
	                        break;
	                    case "A1":
	                    case "A":
	                        targetFolder = new File(downloadDir + File.separator + "Email_Files_" + currentDate + File.separator + "A-Sian");
	                        break;
	                    case "B":
	                    case "B1":
	                        targetFolder = new File(downloadDir + File.separator + "Email_Files_" + currentDate + File.separator + "B-Rowan");
	                        break;
	                    default:
	                    	 targetFolder = new File(downloadDir + File.separator + "Email_Files_" + currentDate + File.separator + "Others");
		                        break;
	                }
 
 
	                    // Ensure the target folder exists
	                    if (!targetFolder.exists()) {
	                        boolean created = targetFolder.mkdir();
	                        if (created) {
	                            System.out.println(targetFolder.getName() + " folder created.");
	                        } else {
	                            System.out.println("Failed to create " + targetFolder.getName() + " folder.");
	                            continue;
	                        }
	                    }
 
	                    // Move the file to the appropriate folder
	                    File targetFile = new File(targetFolder + File.separator + newFileName);
	                    try {
	                        Files.move(renamedFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
	                        System.out.println("Moved " + newFileName + " to " + targetFolder.getName() + " folder.");
	                    } catch (IOException e) {
	                        System.out.println("Failed to move " + newFileName + " to " + targetFolder.getName() + " folder.");
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
