package Pages;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.asis.util.MainClass;

import Driver_manager.DriverManager;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class SaveEmailDraftGraphAPI extends MainClass{

	private static final String SMTP_HOST = "smtp.office365.com"; 
	private static final String SMTP_PORT = "587";
	private static final String USERNAME = "toptechautomation@theoutsourcepro.com.au"; 
	private static final String PASSWORD = "J7OJb*ZwQD25HpC2KO8*n";

	public void saveEmailsAsDraftsFromExcel(String filePath, String downloadsDir) throws IOException {
		File file = new File(filePath);
		if (!file.exists()) {
			throw new IOException("File not found: " + filePath);
		}

		try (FileInputStream fis = new FileInputStream(file);
				Workbook workbook = filePath.endsWith(".xls") ? new HSSFWorkbook(fis) : new XSSFWorkbook(fis)) {

			Sheet sheet = workbook.getSheetAt(0);

			for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
				Row row = sheet.getRow(rowIndex);
				if (row != null) {
					Cell emailCell = row.getCell(6); 
					Cell fileNameCell = row.getCell(7);
					Cell subjectNameCell = row.getCell(2);

					if (emailCell != null && fileNameCell != null) {
						String email = emailCell.getStringCellValue().trim();
						String fileName = fileNameCell.getStringCellValue().trim();
						String subjectName = subjectNameCell.getStringCellValue();
						String subject = fileName;
						String subName = subjectName;
						if (isValidEmail(email)) {
							String filePathToSearch = searchFileWithCorrectExtension(downloadsDir, File.separator + fileName);

							File fileToAttach = new File(filePathToSearch);
							if (fileToAttach.exists()) {
								saveEmailAsDraft(email, subject, filePathToSearch, downloadsDir, subName); 
							} else {
								System.err.println("File not found: " + fileToAttach.getAbsolutePath());
							}
						} else {
							System.err.println("Invalid or missing email address for row " + rowIndex);
						}
					}
				}
			}
		} catch (IOException e) {
			throw new IOException("Error processing the Excel file: " + e.getMessage());
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

	private String searchFileWithCorrectExtension(String downloadsDir, String fileName) {
		File folder = new File(downloadsDir + File.separator + "Downloads");
		File[] files = folder.listFiles();

		if (files != null) {
			for (File file : files) {
				if (file.getName().startsWith(fileName)) {
					String fileExtension = getFileExtension(file);
					if ("pdf".equalsIgnoreCase(fileExtension) || "html".equalsIgnoreCase(fileExtension)) {
						return file.getAbsolutePath();
					}
				}
			}
		}

		File pdfFile = new File(downloadsDir + File.separator + "Downloads" + File.separator + fileName + ".pdf");
		File htmlFile = new File(downloadsDir + File.separator + "Downloads" + File.separator + fileName + ".html");

		if (pdfFile.exists()) {
			return pdfFile.getAbsolutePath();
		} else if (htmlFile.exists()) {
			return htmlFile.getAbsolutePath();
		}

		return downloadsDir + File.separator + "Downloads" + File.separator + fileName + ".pdf";
	}



	private static boolean isValidEmail(String email) {
		return email != null && email.contains("@") && !email.contains(" ") && !email.isEmpty();
	}
	private static void saveEmailAsDraft(String email, String subject, String attachmentPath, String downloadsDir, String subName) {
	    Properties properties = new Properties();
	    properties.put("mail.smtp.host", SMTP_HOST);
	    properties.put("mail.smtp.port", SMTP_PORT);
	    properties.put("mail.smtp.auth", "true");
	    properties.put("mail.smtp.starttls.enable", "true");

	    Session session = Session.getInstance(properties, new Authenticator() {
	        protected PasswordAuthentication getPasswordAuthentication() {
	            return new PasswordAuthentication(USERNAME, PASSWORD);
	        }
	    });

	    try {
	        MimeMessage message = new MimeMessage(session);
	        message.setFrom(new InternetAddress(USERNAME));
	        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
	        message.setSubject(subject);

	        Multipart multipart = new MimeMultipart();

	        BodyPart bodyPart = new MimeBodyPart();
	        String emailBody = getEmailBodyForSubject(subName); // Get email body based on the subject
	        bodyPart.setText(emailBody);
	        multipart.addBodyPart(bodyPart);

	        if (attachmentPath != null && !attachmentPath.isEmpty()) {
	            File attachmentFile = new File(attachmentPath);
	            if (attachmentFile.exists()) {
	                MimeBodyPart attachmentPart = new MimeBodyPart();
	                attachmentPart.attachFile(attachmentFile);
	                multipart.addBodyPart(attachmentPart);
	            }
	        }

	        message.setContent(multipart);

	        File draftsDir = new File(downloadsDir + File.separator + "Downloads" + File.separator + "drafts");
	        if (!draftsDir.exists()) {
	            draftsDir.mkdirs();
	        }

	        String draftPath = draftsDir.getAbsolutePath() + File.separator + subject + ".eml";

	        try (FileOutputStream fos = new FileOutputStream(draftPath)) {
	            message.writeTo(fos);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	// Method to get the email body based on the subject
	private static String getEmailBodyForSubject(String subject) {
	    switch (subject) {
	        case "Confirming your payment plan":
	        	return 	"<html>" +
                		"<body>" +
                		"<img src='cid:notice_of_assessment' style='width:100%;height:auto;'>" +
                		"<p>MyGov</p>" +
                        "<p>MyGov is an online portal for a range of Government services. Where you have registered for a MyGov account and have linked your account to the ATO online services, the ATO will automatically send your amended assessment notice (and any other ATO correspondence) to that account. Please ensure that the preferences in your MyGov are set to notify you when mail has been received in that inbox.</p>" +
                        "<p>You should check your MyGov inbox and ensure that the amended assessment agrees with the estimate we have advised. If the amount differs, please contact us to discuss.</p>" +
                        "<p>If you have not already registered for MyGov, it is a good idea to register and connect to the ATO’s online services. With an increasing number of scams and identity thefts, MyGov is a way of authenticating your status with the regulators in real time and authenticating communication from them. To register, go to <a href='https://my.gov.au'>https://my.gov.au</a>.</p>" +
                        "<p>Thank you for the opportunity to work with you. A tax invoice for our fees relating to this matter is enclosed.</p>" +
                        "<p>If we can assist you with any information on this matter or can assist you in any other way, please do not hesitate to contact us by phoning 08 94307888 or via e-mail to admin@gwcapitalgroup.com.au.</p>"+
                		"<img src='cid:signature' style='width:100%;height:auto;'>" +
                		"</body>" +
                		"</html>";
	        	
	        case "Statement of account - Payment slip":
	            return "Please find attached your statement of account along with the payment slip for your records.";
	        case "Superannuation - Excess non-concessional contributions - Determination":
	            return "This is to notify you regarding excess non-concessional contributions in your superannuation account. See attached for details.";
	        case "New PAYG instalment - Individual / Consolidated group member":
	            return "Your new PAYG instalment details are available. Please review the attached document for further instructions.";
	        case "Notice of assessment - Individual or Trust - EFT refund / Payment due":
	            return "Please find your notice of assessment attached. It includes information regarding your EFT refund or payment due.";
	        case "Income tax - Individual - Tax receipt":
	            return "Your tax receipt for the recent income tax filing is attached. Please keep it for your records.";
	        case "Statement of account - Possible refund":
	            return "Attached is your statement of account which indicates a possible refund. Please review it at your convenience.";
	        case "Lodgment-Overdue-Final warning":
	            return "This is a final warning for an overdue lodgment. Please review the attached document for further action.";
	        case "Foreign investment - residential real estate - capital gains Withholding - clearance certificate":
	            return "Your clearance certificate for capital gains withholding related to foreign investment is attached.";
	        case "Notification of a mistake in your income tax return":
	            return "This email is to notify you of a potential mistake in your income tax return. Please refer to the attached document for details.";
	        default:
	            return "Please find the attached document.";
	    }
	}

	public void closeBrowserXero() {
		DriverManager.getDriver().quit();
	}
}