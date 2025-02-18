package Pages;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.asis.util.ClientExcel;
import com.asis.util.MainClass;
import Driver_manager.DriverManager;

public class XeroSearchingTFN extends MainClass {
	public static String client;
	public static String clientRealName;
	public static String subject;
	public static int wordCount;
	public static String clientIds;
	
	public String emailText = null;
	public String clientCodeText = null;
	public String internal_team=null;
	public String portalname = null;

	@FindBy(xpath = "//button[@title='GlobalSearch']//div[@role='presentation']//*[name()='svg']")
	WebElement searchButton;
	@FindBy(xpath = "//input[@placeholder='Search']")
	WebElement inputBox;
	@FindBy(xpath = "//div[@class='form-item']//div[contains(text(), 'Client Code')]/following-sibling::div")
	WebElement clientCode;
	@FindBy(xpath = "//div[@class='panel-item']//span[contains(text(), 'Email')]/following-sibling::span/a")
	WebElement clientEmail;
	@FindBy(xpath = "//div[@class='panel-item contact h-card']//span[@class='value u-email']//a[@href and @href!='mailto:']")
	WebElement clientEmail2;
	@FindBy(xpath = "a[contains(text(), 'For ATO mails')]")
	WebElement forAtoEmail;
	@FindBy(xpath = "//a[contains(text(), 'For ATO mails')]/following::div[@class='body'][1]")
	WebElement clientEmail3;
	@FindBy(xpath = "//div[contains(@class, 'form-item') and .//div[text()='Internal Team']]//div[@class='value']/span")
	WebElement internalTeam;
	@FindBy(xpath = "//a[normalize-space()='Clients']")
	WebElement Clients;
	@FindBy(xpath = "//i[contains(text(),'All Clients')]")
	WebElement allClients_drpDwn;
	@FindBy(xpath = "//a[contains(text(),'All Clients')]")
	WebElement allClients;
	@FindBy(xpath = "//table//input[@placeholder='Search']")
	WebElement ClientsSearchBox;
	@FindBy(xpath = "//div[@id='button-1021-btnWrap']")
	WebElement clicksearchButton;
	@FindBy(xpath = "//div[@class='name u-truncate']")
	WebElement clientName;
	
	@FindBy(xpath = "//tbody[@id='gridview-1036-body']")
	WebElement searchedTable;
	@FindBy(xpath = "//tbody[@id='gridview-1036-body']//tr")
	WebElement searchedTableRow;
	
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
	
	
	
	public static  void switchportal() throws InterruptedException {
		Thread.sleep(3000);
		try {
			switchPortal_keypoint.click();
			clickPortal.click();
			clickConnect.click();
		}catch(Exception e) {
		}
	}
	public static  void switchportal2() throws InterruptedException {
		Thread.sleep(3000);
		try {
			switchPortal_business.click();
			clickPortal.click();
			clickConnect.click();
		}catch(Exception e) {
			System.out.println("It is already in keypoint portal");
		}
	}
	public XeroSearchingTFN() {
		PageFactory.initElements(DriverManager.getDriver(), this);
	}
	public void clickOnSearchButton() {
		searchButton.click();
	}
	public void inputTheClientNameTFN() throws InterruptedException{
		ClientExcel.readSubjectColumn(filePath);
		ArrayList<String> client_ID =ClientExcel.readSecondColumn(filePath);
		
	    Set<String> specialClientIds = new HashSet<>(Arrays.asList(
	        "569386575", "25629216879", "629216879", "465817223", "166939483", "66620748870",
	        "620748870", "76507391186", "978797270", "64247711224", "674305330", "668568821",
	        "65900948396", "848178201", "69287636643", "678644399", "71662758312", "662758312",
	        "916615714", "78502154572", "636761191", "53641785711", "641785711", "71738283",
	        "11680563473", "680563473", "516358281", "26665785544", "665785544", "55668731628",
	        "80734492", "58107781704","823229331"));
	    
		for (int i = 0; i < client_ID.size(); i++) {
			client = clientNames.get(i);
			wordCount = client.trim().isEmpty() ? 0 : client.trim().split("\\s+").length;
			clientRealName=firstColumn.get(i);
			subject = subjectColumnData.get(i);
			clientIds =client_ID.get(i);
			Thread.sleep(3000);
	        if (specialClientIds.contains(clientIds)){
	            extractClientDetailsTFN(clientIds);// add portal name in excel->pending
	            
	            continue;
	        }
			Thread.sleep(3000);
			try {
				switchportal2();
				wait.until(ExpectedConditions.visibilityOf(Clients));
				Clients.click();
				allClients_drpDwn.click();
				allClients.click();
				Thread.sleep(4000);
				ClientsSearchBox.click();
				ClientsSearchBox.clear();
				ClientsSearchBox.sendKeys(clientIds);
				clicksearchButton.click();
				Thread.sleep(3000);
			}
			catch (Exception e) {
				wait.until(ExpectedConditions.visibilityOf(Clients));
				Clients.click();
				allClients_drpDwn.click();
				allClients.click();
				Thread.sleep(4000);
				wait.until(ExpectedConditions.visibilityOf(ClientsSearchBox));
				ClientsSearchBox.click();
				ClientsSearchBox.clear();
				ClientsSearchBox.sendKeys(clientIds);
				Thread.sleep(3000);
				clicksearchButton.click();
				Thread.sleep(3000);
			}
			boolean clientFound = false;
			try {
				wait.until(ExpectedConditions.visibilityOf(clientName));
				clientName.click();
				clientFound=true;
				ClientExcel.addPortalName("Keypoint");
				extractClientDetails();
			}catch(Exception e){
				searchByNameInKeypoint(client);
			}
		}
	}
	
	private void searchByNameInKeypoint(String client) throws InterruptedException {
		clickOnSearchButton();
		inputBox.clear();
		inputBox.sendKeys(client);
		Thread.sleep(3000);
		
		 boolean clientFound = false;
			try {
				List<WebElement> elements = DriverManager.getDriver().findElements(By.xpath("//a"));

				for (WebElement ele : elements) {
					String elementText = ele.getText();
					String editedWebElementText = elementText.toLowerCase().trim();

					if (elementText.trim().equalsIgnoreCase(client.trim())) {
						System.out.println("Match found in equalsIgnoreCase condition.");
						Thread.sleep(3000);
						ele.click();
						clientFound = true;
						break;
					} else if (editedWebElementText.contains(client.toLowerCase().trim())) {
						System.out.println("Match found in contains condition.");
						Thread.sleep(3000);
						ele.click();
						clientFound = true;
						break;
					} 
				}
				
				if (!clientFound && client.toLowerCase().contains("and")) {
					clientFound = searchClientWithAnd(client.toLowerCase());
				}
				// If client not found and contains a dot
				if (!clientFound && client.contains(".")){
					clientFound = searchClientWithoutDot(client.toLowerCase());
				}
				if(!clientFound && wordCount>=3) {
					clientFound=searchClientByTrimming(client.toLowerCase());
				}
				if (clientFound) {
					extractClientDetails();
					ClientExcel.addPortalName("Keypoint");
				}else {
					searchInSecondaryPortalTFN();
				}
	}catch(Exception e){
		e.printStackTrace();
	}
}
	
	private void searchByNameInBusiness(String client)throws InterruptedException{
		clickOnSearchButton();
		inputBox.clear();
		inputBox.sendKeys(client);
		Thread.sleep(3000);
		
		 boolean clientFound = false;
			try {
				List<WebElement> elements = DriverManager.getDriver().findElements(By.xpath("//a"));

				for (WebElement ele : elements) {
					String elementText = ele.getText();
					String editedWebElementText = elementText.toLowerCase().trim();

					if (elementText.trim().equalsIgnoreCase(client.trim())) {
						System.out.println("Match found in equalsIgnoreCase condition.");
						Thread.sleep(3000);
						ele.click();
						clientFound = true;
						break;
					} else if (editedWebElementText.contains(client.toLowerCase().trim())) {
						System.out.println("Match found in contains condition.");
						Thread.sleep(3000);
						ele.click();
						clientFound = true;
						break;
					} 
				}
				
				if (!clientFound && client.toLowerCase().contains("and")) {
					clientFound = searchClientWithAnd(client.toLowerCase());
				}
				// If client not found and contains a dot
				if (!clientFound && client.contains(".")){
					clientFound = searchClientWithoutDot(client.toLowerCase());
				}
				if(!clientFound && wordCount>=3) {
					clientFound=searchClientByTrimming(client.toLowerCase());
				}
				if (clientFound) {
					extractClientDetails();
					ClientExcel.addPortalName("Business");
				}else {
					handleClientNotFound(clientRealName,subject);
					ClientExcel.addPortalName("Not found in any portal");
				}
	}catch(Exception e){
		e.printStackTrace();
	}
		
		
	}
	
	private void searchInSecondaryPortalTFN() throws InterruptedException{
		
			try {
				switchportal();
				Thread.sleep(5000);
				wait.until(ExpectedConditions.visibilityOf(Clients));
				Clients.click();
				allClients_drpDwn.click();
				allClients.click();
				ClientsSearchBox.click();
				ClientsSearchBox.clear();
				ClientsSearchBox.sendKeys(clientIds);
				clicksearchButton.click();
				Thread.sleep(3000);
			}
			catch(Exception e) {
				wait.until(ExpectedConditions.visibilityOf(Clients));
				Clients.click();
				allClients_drpDwn.click();
				allClients.click();
				ClientsSearchBox.click();
				ClientsSearchBox.clear();
				ClientsSearchBox.sendKeys(clientIds);
				clicksearchButton.click();
				Thread.sleep(3000);
			}
			boolean clientFound = false;
			try {
				wait.until(ExpectedConditions.visibilityOf(clientName));
				clientName.click();
				clientFound=true;
			}catch(Exception e) {  
				System.out.println("client is not found on Xero");
			}
			if(clientFound) {
				extractClientDetails();
				ClientExcel.addPortalName("Business");				
			}else {
				searchByNameInBusiness(client);
//				handleClientNotFound(clientRealName,subject);
//				ClientExcel.addPortalName("Not found in any portal");
			}
		
		
	}
	
	
	private void extractClientDetailsTFN(String clientIds) {
	    switch (clientIds) {
	        case "569386575":
	        case "25629216879":
	        case "629216879":
	            emailText = "nina@cedarfoundation.org";
	            clientCodeText = "HJORCP01";
	            internal_team = "B1";
	            portalname="Keypoint";
	            break;
	        case "465817223":
	            emailText = "shell_and_paul@hotmail.com";
	            clientCodeText = "DEL0406";
	            internal_team = "no teamName";
	            portalname="Keypoint";
	            break;
	        case "166939483":
	        case "66620748870":
	        case "620748870":
	            emailText = "no email found";
	            clientCodeText = "KIDDCP04";
	            internal_team = "no teamName";
	            portalname="Keypoint";
	            break;
	        case "76507391186":
	            emailText = "admin@karrathacountryclub.com.au";
	            clientCodeText = "KARRCOUN";
	            internal_team = "no teamName";
	            portalname="Keypoint";
	            break;
	        case "978797270":
	        case "64247711224":
	            emailText = "no email found";
	            clientCodeText = "KIDDFT02";
	            internal_team = "no teamName";
	            portalname="Keypoint";
	            break;
	        case "674305330":
	            emailText = "russell.hodson@alldin.com.au";
	            clientCodeText = "HODSCP07";
	            internal_team = "A1";
	            portalname="Keypoint";
	            break;
	        case "668568821":
	            emailText = "amanda.ridout@hotmail.com";
	            clientCodeText = "RIDOCP02";
	            internal_team = "A1";
	            portalname="Keypoint";
	            break;
	        case "65900948396":
	            emailText = "admin@shekinahgloryhealthcare.com.au";
	            clientCodeText = "no client code";
	            internal_team = "no teamName";
	            portalname="Keypoint";
	            break;
	        case "848178201":
	        case "69287636643":
	            emailText = "kirianabarclay@gmail.com";
	            clientCodeText = "BARCTR01";
	            internal_team = "C1";
	            portalname="Keypoint";
	            break;
	        case "678644399":
	        case "71662758312":
	        case "662758312":
	            emailText = "info@allaspectsceilings.com";
	            clientCodeText = "BARCCP01";
	            internal_team = "C1";
	            portalname="Keypoint";
	            break;
	        case "916615714":
	        case "78502154572":
	            emailText = "joe.allen1980@yahoo.com.au";
	            clientCodeText = "ALLEFTRU";
	            internal_team = "B";
	            portalname="Keypoint";
	            break;
	        case "636761191":
	        case "53641785711":
	        case "641785711":
	            emailText = "no email found";
	            clientCodeText = "KIDDCP05";
	            internal_team = "C1";
	            portalname="Keypoint";
	            break;
	        case "71738283":
	            emailText = "delest@bigpond.com";
	            clientCodeText = "DEL0206";
	            internal_team = "C1";
	            portalname="Keypoint";
	            break;
	        case "11680563473":
	        case "680563473":
	            emailText = "no email found";
	            clientCodeText = "no client code";
	            internal_team = "D";
	            portalname="Business";
	            break;
	        case "516358281":
	        case "26665785544":
	        case "665785544":
	            emailText = "richie18thunder@gmail.com";
	            clientCodeText = "no client code";
	            internal_team = "no teamName";
	            portalname="Business";
	            break;
	        case "55668731628":
	            emailText = "no email found";
	            clientCodeText = "no client code";
	            internal_team = "no teamName";
	            portalname="Business";
	            break;
	        case "80734492":
	        case "58107781704":
	            emailText = "alison@abcau.com.au";
	            clientCodeText = "YULECORP";
	            internal_team = "A1";
	            portalname="Business";
	            break;
	        case "823229331":
	        	emailText = "candiceheapes@bigpond.com";
	            clientCodeText = "HEA0102";
	            internal_team = "C1";
	            portalname="Keypoint";
	            break;
	        case "63679209888":
	        	emailText = "Jamie.Poole@cmscepcor.com";
	            clientCodeText = "CMSCCP01";
	            internal_team = "B1";
	            portalname="Keypoint";
	            break;  
	        case "35883565289":
	        	emailText = "bjramsay27@gmail.com";
	            clientCodeText = "RAMSBRON";
	            internal_team = "A";
	            portalname="Keypoint";
	            break; 
	            
	        default:
	            emailText = "no email found";
	            clientCodeText = "no client code";
	            internal_team = "no teamName";
	            portalname="Not found in any portal";
	           
	    }

	    // Printing extracted details
	    System.out.println("Client ID: " + clientIds);
	    System.out.println("Email: " + emailText);
	    System.out.println("Client Code: " + clientCodeText);
	    System.out.println("Internal Team: " + internal_team);
	    System.out.println("PortalName: " + portalname);

	    ClientExcel.addClientData(clientCodeText, emailText, internal_team);
	    ClientExcel.writeCombinedDataToExcel(clientCodeText, subject);
	    ClientExcel.addPortalName(portalname);
	    ClientExcel.saveExcelFile();
	}

	private void extractClientDetails() {
		try {
			wait.until(ExpectedConditions.visibilityOf(clientEmail));
			Thread.sleep(4000);
			
			emailText = clientEmail.getText().trim();
			System.out.println(emailText);
		} catch (Exception e1) {
			try {
				System.out.println("Email 1 not found");
				wait.until(ExpectedConditions.visibilityOf(clientEmail2));
				Thread.sleep(4000);
				emailText = clientEmail2.getText().trim();
				System.out.println(emailText);
			} catch (Exception e2) {
				try{
					System.out.println("Email 2 not found");
					wait.until(ExpectedConditions.visibilityOf(forAtoEmail));
					Thread.sleep(4000);
					emailText = clientEmail3.getText().trim();
					System.out.println(emailText);
				}catch(Exception e3) {
					System.out.println("Email 3 not found");
					emailText = "no email found";
					System.out.println(emailText);
				}
			}
		}
		try {
			wait.until(ExpectedConditions.visibilityOf(clientCode));
			if (clientCode.isDisplayed()) {
				clientCodeText = clientCode.getText().trim();
			}
			if(clientCode.getText().trim().contains("-")) {
				clientCodeText = clientRealName;
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
		}

		ClientExcel.addClientData(clientCodeText, emailText, internal_team);
		ClientExcel.writeCombinedDataToExcel(clientCodeText, subject);
	}
	private boolean searchClientByTrimming(String clientName) throws InterruptedException {
	    boolean clientFound = false;
	    int lastSpaceIndex = clientName.lastIndexOf(" ");

	    if (lastSpaceIndex == -1) {
	        return false; // No words to trim
	    }

	    // Remove only the last word
	    String modifiedClientName = clientName.substring(0, lastSpaceIndex).trim();

	    // Search with the last word removed
	    inputBox.clear();
	    inputBox.sendKeys(modifiedClientName);
	    Thread.sleep(3000);

	    List<WebElement> elements = DriverManager.getDriver().findElements(By.xpath("//a"));
	    for (WebElement ele : elements) {
	        String elementText = ele.getText().trim();
	        if (elementText.equalsIgnoreCase(modifiedClientName) || elementText.toLowerCase().contains(modifiedClientName.toLowerCase())) {
	            System.out.println(modifiedClientName + " found by removing the last word.");
	            Thread.sleep(3000);
	            ele.click();
	            clientFound = true;
	            break; // Stop searching once found
	        }
	    }

	    return clientFound; // Return true if found, otherwise false
	} 
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
				System.out.println(client+" it is found in contains .and replace  method");
				Thread.sleep(3000);
				ele.click();
				clientFound = true;
				break;
			}
		}
		return clientFound;
	}
	private boolean searchClientWithAnd(String clientName) throws InterruptedException {
		boolean clientFound = false;
		String clientWithAnd = clientName.replace("and", "&").trim();
		inputBox.clear();
		inputBox.sendKeys(clientWithAnd);
		Thread.sleep(3000);

		List<WebElement> elements = DriverManager.getDriver().findElements(By.xpath("//a"));
		for (WebElement ele : elements) {
			String elementText = ele.getText().trim();
			if (elementText.equalsIgnoreCase(clientWithAnd) || elementText.toLowerCase().contains(clientWithAnd.toLowerCase())) {
				System.out.println(client+" it is found in contains and replace with & method");
				Thread.sleep(3000);
				ele.click();
				clientFound = true;
				break;
			}
		}
		return clientFound;
	}
	private void handleClientNotFound(String clientName,String subject) throws InterruptedException {
		Thread.sleep(3000);
		ClientExcel.addClientData("client name not found", "client name not found", "no teamName");
		ClientExcel.writeCombinedDataToExcel(clientName, subject);
		ClientExcel.saveExcelFile();
	}

	public static String sanitizeFileName(String fileName) {
		// Define a regex pattern to match special characters \ / : * ? " < > |
		String specialCharactersPattern = "[\\\\/:*?\"<>|]";
		// Replace the special characters with a single space
		return fileName.replaceAll(specialCharactersPattern, " ");
	}

	public void renameAndMovePdfFilesToDownloadsFolder(String downloadDir) throws InterruptedException {
	    ArrayList<String> pdfFileNames = ClientExcel.readPdfFileNamesFromColumn8(filePath);
	    ArrayList<String> fileNamesColumn7 = ClientExcel.readFileNamesFromColumn7(filePath);
	    ArrayList<String> teamNames = ClientExcel.readTeamNamesFromColumn9(filePath);  
	    ArrayList<String> portalDataNames = ClientExcel.readPortalColumn(filePath);  

	    if (pdfFileNames.size() != fileNamesColumn7.size() || pdfFileNames.size() != teamNames.size() || pdfFileNames.size() != portalDataNames.size()) {
	        System.out.println("Size mismatch in lists! Check data.");
	        return;
	    }

	    File downloadsFolder = new File(downloadDir + File.separator + "Email_Files_" + currentDate);
	    File downloadsFolderD = new File(downloadDirD + File.separator + "Email_Files_" + currentDate);
	    downloadsFolder.mkdirs();
	    downloadsFolderD.mkdirs();

	    int cnt = 0;
	    for (String pdfFileName : pdfFileNames) {
	        System.out.println("Processing file: " + pdfFileName);

	        String fullPath = downloadDir + File.separator + pdfFileName.trim();
	        File pdfFile = new File(fullPath);
	        Thread.sleep(3000);
	        if (!pdfFile.exists()) {
	            System.out.println("File not found: " + fullPath);
	            continue;
	        }

	        String currentExtension = getFileExtension(pdfFile);
	        if (cnt >= fileNamesColumn7.size()) {
	            System.out.println("Skipping index " + cnt + " due to size mismatch.");
	            continue;
	        }

	        String newFileName = fileNamesColumn7.get(cnt) + "." + currentExtension;
	        File renamedFile = new File(downloadDir + File.separator + newFileName);
	        
	        int fileCount = 1;
	        while (renamedFile.exists()) {
	            newFileName = "new_" + fileNamesColumn7.get(cnt) + "_" + fileCount + "." + currentExtension;
	            renamedFile = new File(downloadDir + File.separator + sanitizeFileName(newFileName));
	            fileCount++;
	        }

	        String portalName = portalDataNames.get(cnt).replaceAll("\\s+", " ").trim();
	        System.out.println("Checking portal name: '" + portalName + "' at index " + cnt);

	        String DownloadsDirectory = portalName.equalsIgnoreCase("Keypoint") ? downloadDir : downloadDirD;
	        System.out.println("Using directory: " + DownloadsDirectory);

	        if (pdfFile.renameTo(renamedFile)) {
	            String teamName = teamNames.get(cnt).trim();
	            File targetFolder;
	            switch (teamName) {
	                case "K": targetFolder = new File(DownloadsDirectory + File.separator + "Email_Files_" + currentDate + File.separator + "K-Lindy"); 
	                break;
	                case "C":
	                case "C1": targetFolder = new File(DownloadsDirectory + File.separator + "Email_Files_" + currentDate + File.separator + "C-Rebecca");
	                break;
	                case "A1":
	                case "A": targetFolder = new File(DownloadsDirectory + File.separator + "Email_Files_" + currentDate + File.separator + "A-Sian"); 
	                break;
	                case "B":
	                case "B1": targetFolder = new File(DownloadsDirectory + File.separator + "Email_Files_" + currentDate + File.separator + "B-Rowan");
	                break;
	                case "D": targetFolder = new File(DownloadsDirectory + File.separator + "Email_Files_" + currentDate + File.separator + "D-Melvyn"); 
	                break;
	                default: targetFolder = new File(DownloadsDirectory + File.separator + "Email_Files_" + currentDate + File.separator + "Others");
	                break;
	            }

	            targetFolder.mkdirs();
	            File targetFile = new File(targetFolder + File.separator + newFileName);
	            try {
	                Files.move(renamedFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
	                System.out.println("Moved file to: " + targetFile.getAbsolutePath());
	            } catch (IOException e) {
	                System.out.println("Failed to move file: " + e.getMessage());
	            }
	        } else {
	            System.out.println("Failed to rename file: " + pdfFile.getAbsolutePath());
	        }
	        cnt++;
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