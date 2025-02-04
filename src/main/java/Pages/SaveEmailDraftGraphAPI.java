package Pages;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.asis.util.ClientExcel;
import com.asis.util.MainClass;

import Driver_manager.DriverManager;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class SaveEmailDraftGraphAPI extends MainClass {

    private static final String SMTP_HOST = "smtp.office365.com";
    private static final String SMTP_PORT = "587";
    private static final String USERNAME = "toptechautomation@theoutsourcepro.com.au";
    private static final String PASSWORD = "J7OJb*ZwQD25HpC2KO8*n";

    public void saveEmailsAsDraftsFromExcel(String filePath, String downloadsDir) throws IOException {
        
        processEmailsForDownloadDir(filePath, downloadDir);
        processEmailsForDownloadDir(filePath, downloadDirD);
    }
    private void processEmailsForDownloadDir(String filePath, String downloadsDir) throws IOException {
    	File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("File not found: " + filePath);
        }
        File mainFolder = new File(downloadsDir + File.separator + "Email_Files_" + currentDate);
        if (!mainFolder.exists() || !mainFolder.isDirectory()) {
            throw new IOException("Main folder not found: " + mainFolder.getAbsolutePath());
        }

        List<String> teamNames = ClientExcel.readTeamNamesFromColumn9(filePath);

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = filePath.endsWith(".xls") ? new HSSFWorkbook(fis) : new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row != null) {
                    Cell emailCell = row.getCell(6);
                    Cell fileNameCell = row.getCell(7);
                    Cell emailRcvrName = row.getCell(0);

                    if (emailCell != null && fileNameCell != null) {
                        String email = emailCell.getStringCellValue().trim();
                        String fileName = fileNameCell.getStringCellValue().trim();
                        String subject = fileName;
                        String teamName = teamNames.get(rowIndex - 1).trim();
                        
                        String emailRcvr = emailRcvrName.getStringCellValue().trim();

                        String emailScndWrd="";
                        if (emailRcvr.toLowerCase().contains("trust")) {
                            emailScndWrd = "Trustee(s)\n" + emailRcvr; 
                        }
                        else if(emailRcvr.toLowerCase().contains("pty ltd")) {
                            emailScndWrd = "Director(s)\n" + emailRcvr; 
                        }
                        else if (emailRcvr.contains(",")) {
                            // Extract all words after the comma
                            emailScndWrd = emailRcvr.substring(emailRcvr.indexOf(",") + 1).trim();
                        } 
                        
                        
                   
                        if (isValidEmail(email)) {
                            String filePathToSearch = searchFileInSubfolders(mainFolder, fileName);

                            File fileToAttach = new File(filePathToSearch);
                            if (fileToAttach.exists()) {
                                saveEmailAsDraft(email, subject, filePathToSearch, downloadsDir, teamName,emailScndWrd);
                            } else {
                                //System.err.println("File not found: " + fileToAttach.getAbsolutePath());
                            }
                        } else {
                            //System.err.println("Invalid or missing email address for row " + rowIndex);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new IOException("Error processing the Excel file: " + e.getMessage());
        }
    }

    private String searchFileInSubfolders(File mainFolder, String fileName) {
        File[] subfolders = mainFolder.listFiles(File::isDirectory);
        if (subfolders != null) {
            for (File subfolder : subfolders) {
                File file = new File(subfolder, fileName + ".pdf");
                if (file.exists()) {
                    return file.getAbsolutePath();
                }
                file = new File(subfolder, fileName + ".html");
                if (file.exists()) {
                    return file.getAbsolutePath();
                }
            }
        }
        return ""; // Return an empty string if the file is not found
    }

    private static boolean isValidEmail(String email) {
        return email != null && email.contains("@") && !email.contains(" ") && !email.isEmpty();
    }

    private static void saveEmailAsDraft(String email, String subject, String attachmentPath, String downloadsDir, String teamName, String emailRcvrNm) {
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
            
         // Set the email body with the provided template
            String emailBody = "Dear " + emailRcvrNm + " ,\n\n" +
                "Hope you are well. Please see attached, correspondence from the Australian Taxation Office.\n\n" +
                "It is important, you read it.\n\n" +
                "1. If you have already actioned this or paid this account, please keep this letter only for your record.\n" +
                "2. If an action needs to be taken, please take so in line with the letter attached.\n" +
                "3. If you have any queries or need assistance, please email us at correspodence@fortunaadvisors.com.au or direct to your client manager.\n\n" +
                "Kind Regards,\n" +
                "Natalie Nicolaou\n" +
                "Administrator";
            
            
            BodyPart bodyPart = new MimeBodyPart();
            bodyPart.setText(emailBody);
            multipart.addBodyPart(bodyPart);

            if (attachmentPath != null && !attachmentPath.isEmpty()){
                File attachmentFile = new File(attachmentPath);
                if (attachmentFile.exists()) {
                    MimeBodyPart attachmentPart = new MimeBodyPart();
                    attachmentPart.attachFile(attachmentFile);
                    multipart.addBodyPart(attachmentPart);
                }
            }

            message.setContent(multipart);

            String teamFolder = determineTeamFolder(teamName);
            File draftsDir = new File(downloadsDir + File.separator + "Email_Files_" + currentDate + File.separator + teamFolder + File.separator + "Draft_Folder_" + currentDate);
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

    private static String determineTeamFolder(String teamName) {
        switch (teamName) { 
            case "A":
            case "A1":
                return "A-Sian";
            case "B":
            case "B1":
                return "B-Rowan";
            case "C":
            case "C1":
                return "C-Rebecca";
            case "K":
                return "K-Lindy";
            case "D":
            	return "D-Melvyn";
            default:
            	 return "Others"; // Default folder for undefined team names
        }
    }

    public void closeBrowserXero() {
        DriverManager.getDriver().quit();
    }
}