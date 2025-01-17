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

	@FindBy(xpath="//div[@class='xnav-appbutton--body']")
	WebElement switchPortal;
	
	@FindBy(xpath="//a[normalize-space()='Portal']")
	WebElement clickPortal;
	
	@FindBy(xpath = "//input[@id='ctl00_PageContent_btnAction_60fb5410-fab7-45b1-8755-711948782a78']")
	WebElement clickConnect;
	
	@FindBy(xpath = "//button[@title='GlobalSearch']//div[@role='presentation']//*[name()='svg']")
	WebElement searchButton2;
	
	@FindBy(xpath = "//input[@placeholder='Search']")
	WebElement inputBox2;
	
	public XeroSearchClientPageJJ() {
		PageFactory.initElements(DriverManager.getDriver(), this);
	}

	public void clickOnSearchButton() {
		searchButton.click();
	}
	 public void clickOnSearchButton2() {
		 searchButton2.click();
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
			if(switchPortal.getText().trim().contains("Fortuna Accountants & Business")) {
				switchPortal.click();
				clickPortal.click();
				clickConnect.click();
			}
			
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
				    String elementText = ele.getText();
				    String editedWebElementText=elementText.toLowerCase().trim();
				    //System.out.println("Raw element text: '" + elementText);
				    //System.out.println("Processed element text: '" + elementText.toLowerCase().trim());
				    //System.out.println("Client: '" + client.toLowerCase().trim());
				    
				    if (elementText.trim().equalsIgnoreCase(client.trim())) {
				        System.out.println("Match found in equalsIgnoreCase condition.");
				        Thread.sleep(2000);
				        ele.click();
				        clientFound = true;
				        break;
				    } else if (elementText.toLowerCase().trim().contains(client.toLowerCase().trim())) {
				        System.out.println("Match found in contains condition.");
				        Thread.sleep(2000);
				        ele.click();
				        clientFound = true;
				        break;
				    } else {
				    	if(editedWebElementText.contains(" & ")){
				    		//System.out.println("yes it contains ' & ' ");
				    		String removeAndfrmText = editedWebElementText.replaceAll("\\s*&\\s*", "&");
				    		
				    		//System.out.println("Text after removing spaces around '&': " + removeAndfrmText);
				    	    //System.out.println("Client to match: " + client.toLowerCase().trim());
				    	    
				    		if(removeAndfrmText.contains(client.toLowerCase().trim())) {
				    			Thread.sleep(2000);
						        ele.click();
						        clientFound = true;
						        break;
				    		}
				    	}else {
				    		System.out.println("No match found for this element.");
				    	}
				        
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
							else if(ele.getText().toLowerCase().trim().contains(clientWithoutDot.toLowerCase().trim())) {
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
				if (clientFound==true) {
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
					
					// check if internal team is visible 
					try {
						wait.until(ExpectedConditions.visibilityOf(internalTeam));
						if (internalTeam.isDisplayed()) {
							internal_team = internalTeam.getText().trim();
						}
					}
					// if internal team is not visible
					catch (Exception e) {
						System.out.println("Internal team is not there.");
					}	
					
					


					if (emailText != null && clientCodeText != "-" && internal_team != null){
						ClientExcel.addClientData(clientCodeText, emailText, internal_team);
						ClientExcel.writeCombinedDataToExcel(clientCodeText, subject);
					
						clickOnSearchButton();
						
					} 
					else if(emailText != null && clientCodeText != "-" && internal_team == null){
						ClientExcel.addClientData(clientCodeText, emailText, "no teamName");
						ClientExcel.writeCombinedDataToExcel(clientCodeText, subject);
						clickOnSearchButton();
						
					}
					else if(emailText == null  && clientCodeText != "-" && internal_team != null) {
						ClientExcel.addClientData(clientCodeText, "no email found",internal_team);
						ClientExcel.writeCombinedDataToExcel(clientCodeText, subject);
						clickOnSearchButton();
					}
					else {
						ClientExcel.addClientData("client code not found", "client email not found","no teamName");
						ClientExcel.writeCombinedDataToExcel("null", subject);
						ClientExcel.saveExcelFile();
						clickOnSearchButton();
					}
					
				} 
				
				else {
					if(clientFound == false) {
						Thread.sleep(3000);
						switchPortal.click();
						clickPortal.click();
						clickConnect.click();
						
						try {
							clickOnSearchButton2();
							inputBox2.clear();
							inputBox2.sendKeys(client);
							Thread.sleep(3000);
						}
						// if search button will not work
						catch(Exception e) {
							clickOnSearchButton2();
							inputBox2.clear();
							inputBox2.sendKeys(client);
							Thread.sleep(3000);
						}
						
						try {
							List<WebElement> elements2 = DriverManager.getDriver().findElements(By.xpath("//a"));
							boolean clientFound2 = false;
							
							for (WebElement ele : elements2) {
							    String elementText = ele.getText();
							    String editedWebElementText=elementText.toLowerCase().trim();
							    
							    if (elementText.trim().equalsIgnoreCase(client.trim())) {
							        System.out.println("Match found in equalsIgnoreCase condition.");
							        Thread.sleep(2000);
							        ele.click();
							        clientFound2 = true;
							        break;
							    } else if (elementText.toLowerCase().trim().contains(client.toLowerCase().trim())) {
							        System.out.println("Match found in contains condition.");
							        Thread.sleep(2000);
							        ele.click();
							        clientFound2 = true;
							        break;
							    } else {
							    	if(editedWebElementText.contains(" & ")){
							    		//System.out.println("yes it contains ' & ' ");
							    		String removeAndfrmText = editedWebElementText.replaceAll("\\s*&\\s*", "&");
							    		
							    		//System.out.println("Text after removing spaces around '&': " + removeAndfrmText);
							    	    //System.out.println("Client to match: " + client.toLowerCase().trim());
							    	    
							    		if(removeAndfrmText.contains(client.toLowerCase().trim())) {
							    			Thread.sleep(2000);
									        ele.click();
									        clientFound2 = true;
									        break;
							    		}
							    	}else {
							    		System.out.println("No match found for this element.");
							    	}
							        
							    }
							}
							
							if (!clientFound2 && client.contains(".")) {
								String clientWithoutDot = client.replace(".", "").trim();

								// if client have . in their name 
								try {
									inputBox2.clear();
									inputBox2.sendKeys(clientWithoutDot);
									Thread.sleep(3000);
									elements = DriverManager.getDriver().findElements(By.xpath("//a"));
									for (WebElement ele : elements2) {
										if (ele.getText().trim().equalsIgnoreCase(clientWithoutDot)) {
											ele.click();
											clientFound2 = true;
											break;
										}
										else if(ele.getText().toLowerCase().trim().contains(clientWithoutDot.toLowerCase().trim())) {
											ele.click();
											clientFound2 = true;
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
							if (clientFound2==true) {
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
								
								// check if internal team is visible 
								try {
									wait.until(ExpectedConditions.visibilityOf(internalTeam));
									if (internalTeam.isDisplayed()) {
										internal_team = internalTeam.getText().trim();
									}
								}
								// if internal team is not visible
								catch (Exception e) {
									System.out.println("Internal team is not there.");
								}	
								
								


								if (emailText != null && clientCodeText != "-" && internal_team != null){
									ClientExcel.addClientData(clientCodeText, emailText, internal_team);
									ClientExcel.writeCombinedDataToExcel(clientCodeText, subject);
									switchPortal.click();
									clickPortal.click();
									clickConnect.click();
									clickOnSearchButton();
									
								} 
								else if(emailText != null && clientCodeText != "-" && internal_team == null){
									ClientExcel.addClientData(clientCodeText, emailText, "no teamName");
									ClientExcel.writeCombinedDataToExcel(clientCodeText, subject);
									switchPortal.click();
									clickPortal.click();
									clickConnect.click();
									clickOnSearchButton();
									
								}else if(emailText == null && clientCodeText != "-" && internal_team != null) {
									ClientExcel.addClientData(clientCodeText, "no email found", "no teamName");
									ClientExcel.writeCombinedDataToExcel(clientCodeText, subject);
									switchPortal.click();
									clickPortal.click();
									clickConnect.click();
									clickOnSearchButton();
								}
								else {
									ClientExcel.addClientData("client code not found", "client email not found","no teamName");
									ClientExcel.writeCombinedDataToExcel("null", subject);
									ClientExcel.saveExcelFile();
									switchPortal.click();
									clickPortal.click();
									clickConnect.click();
									clickOnSearchButton();
								}
								
							} else {
								Thread.sleep(3000);
								ClientExcel.addClientData("client code not found", "client name not found","no teamName");
								ClientExcel.writeCombinedDataToExcel("null", subject);
								ClientExcel.saveExcelFile();
								switchPortal.click();
								clickPortal.click();
								clickConnect.click();
								
								Thread.sleep(3000);
							}
							
						}catch(Exception e ) {
							
						}
						
						
						
					}else {
						Thread.sleep(3000);
						ClientExcel.addClientData("client code not found", "client name not found","no teamName");
						ClientExcel.writeCombinedDataToExcel("null", subject);
						ClientExcel.saveExcelFile();
						clickOnSearchButton();
					}
					
					
					
				}
			
				
		} 
			// if client name is not visible on the sear directory
			catch (Exception e) {
				e.printStackTrace();
			}
		}
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
