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

public class SaveEmailDraftGraphAPI_New extends MainClass {

    private static final String SMTP_HOST = "smtp.office365.com";
    private static final String SMTP_PORT = "587";
    private static final String USERNAME = "toptechautomation@theoutsourcepro.com.au";
    private static final String PASSWORD = "J7OJb*ZwQD25HpC2KO8*n";

    public static String downloadDirE = "E:" + File.separator + "ATO Email files" + File.separator;
    public static String downloadDirK = "K:" + File.separator + "ATO Email files" + File.separator;

    public void saveEmailsAsDraftsFromExcel(String filePath) throws IOException {
        List<String> portalTypes = ClientExcel.readPortalColumn(filePath);
        List<String> teamNames = ClientExcel.readTeamNamesFromColumn9(filePath);

        try (FileInputStream fis = new FileInputStream(filePath);
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
                        String emailScndWrd = determineRecipientTitle(emailRcvr);
                        String portalType = portalTypes.get(rowIndex - 1).trim(); // Get portal type for this row

                        // Determine the correct folder based on portal type
                        File mainFolder;
                        if ("Keypoint".equalsIgnoreCase(portalType)) {
                            mainFolder = new File(downloadDirK + File.separator + "Email_Files_" + currentDate);
                        } else if ("Business".equalsIgnoreCase(portalType)) {
                            mainFolder = new File(downloadDirE + File.separator + "Email_Files_" + currentDate);
                        } else {
                            continue; // Skip row if portal type is unknown
                        }

                        if (isValidEmail(email)) {
                            String filePathToSearch = searchFileInSubfolders(mainFolder, fileName);
                            File fileToAttach = new File(filePathToSearch);
                            if (fileToAttach.exists()) {
                                saveEmailAsDraft(email, subject, filePathToSearch, teamName, emailScndWrd, mainFolder);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new IOException("Error processing the Excel file: " + e.getMessage());
        }
    }

    private String determineRecipientTitle(String emailRcvr) {
        if (emailRcvr.toLowerCase().contains("trust")) {
            return "Trustee(s)\n" + emailRcvr;
        } else if (emailRcvr.toLowerCase().contains("pty ltd")) {
            return "Director(s)\n" + emailRcvr;
        } else if (emailRcvr.contains(",")) {
            return emailRcvr.substring(emailRcvr.indexOf(",") + 1).trim();
        }
        return "";
    }

    private String searchFileInSubfolders(File mainFolder, String fileName) {
        File[] subfolders = mainFolder.listFiles(File::isDirectory);
        if (subfolders != null) {
            for (File subfolder : subfolders) {
                File file = new File(subfolder, fileName + ".pdf");
                if (file.exists()) return file.getAbsolutePath();
                file = new File(subfolder, fileName + ".html");
                if (file.exists()) return file.getAbsolutePath();
            }
        }
        return "";
    }

    private static boolean isValidEmail(String email) {
        return email != null && email.contains("@") && !email.contains(" ") && !email.isEmpty();
    }

    private static void saveEmailAsDraft(String email, String subject, String attachmentPath, String teamName, String emailRcvrNm, File mainFolder) {
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
            // + teamFolder + File.separator +
            File draftDir = new File(mainFolder, teamFolder + File.separator + "Draft_Folder_" + currentDate);
            if (!draftDir.exists()) {
                draftDir.mkdirs();
            }
            try (FileOutputStream fos = new FileOutputStream(new File(draftDir, subject + ".eml"))) {
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