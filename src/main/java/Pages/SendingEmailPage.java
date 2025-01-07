package Pages;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SendingEmailPage {
    public static void main(String[] args) {
        String excelFilePath = "C:\\Users\\TOP\\Documents\\TestingDocuments.xlsx";

        String dwnldDir_Notfowd = "C:/ATO Email files/Email_Files_2025-01-07/Notfowd";
        String dwnldDir_Rowan = "C:/ATO Email files/Email_Files_2025-01-07/Rowan";
        String dwnldDir_Rebecca = "C:/ATO Email files/Email_Files_2025-01-07/Rebecca";
        String dwnldDir_Slain = "C:/ATO Email files/Email_Files_2025-01-07/Slain";

        String fromEmail = "toptechautomation@theoutsourcepro.com.au";
        String password = "J7OJb*ZwQD25HpC2KO8*n";
        String body = "Dear «      »,\n\nHope you are well. Please see attached, correspondence from the Australian Taxation Office.\n\nIt is important, you read it.\n\n1. If you have already actioned this or paid this account, please keep this letter only for your record.\n\n2. If an action needs to be taken, please take so in line with the letter attached.\n\n3. If you have any queries or need assistance, please email us at correspodence@fortunaadvisors.com.au or direct to your client manager.\n\nKind Regards, \nNatalie Nicolaou\nAdministrator";

        try {
            FileInputStream file = new FileInputStream(excelFilePath);
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                Cell emailCell = row.getCell(6);   // Column 6 for Email ID
                Cell fileCell = row.getCell(7);    // Column 7 for the file name (subject and attachment)
                Cell internalTeamCell = row.getCell(9); // Column 9 for internal team info

                if (emailCell != null && fileCell != null && internalTeamCell != null) {
                    String recipientEmail = emailCell.getStringCellValue();
                    String fileName = fileCell.getStringCellValue();
                    String internalTeam = internalTeamCell.getStringCellValue().trim();  // Get internal team info

                    String downloadDir = getDownloadDir(internalTeam, dwnldDir_Slain, dwnldDir_Rowan, dwnldDir_Rebecca, dwnldDir_Notfowd);
                    if (downloadDir != null) {
                        String fileExtension = getFileExtension(fileName, downloadDir);  // Get file extension dynamically
                        if (!fileExtension.isEmpty()) {
                            File attachment = new File(downloadDir, fileName + fileExtension);

                            // Check if the file exists before proceeding with email sending
                            if (attachment.exists()) {
                                if (validateEmail(recipientEmail)) {
                                    sendEmail(fromEmail, password, recipientEmail, fileName, body, attachment);
                                } else {
                                    System.out.println("Invalid email format: " + recipientEmail + ". Skipping...");
                                }
                            } else {
                                System.out.println("Attachment file not found: " + attachment.getAbsolutePath());
                            }
                        } else {
                            System.out.println("No valid file found for: " + fileName + " in directory: " + downloadDir);
                        }
                    } else {
                        System.out.println("Invalid internal team value: " + internalTeam + " at row " + i + ". Skipping...");
                    }

                }
            }
            workbook.close();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Email validation method
    public static boolean validateEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // Method to get the download directory based on internal team
    public static String getDownloadDir(String internalTeam, String dwnldDir_Slain, String dwnldDir_Rowan, String dwnldDir_Rebecca, String dwnldDir_Notfowd) {
        switch (internalTeam) {
            case "A":
            case "A1":
                return dwnldDir_Slain;
            case "B":
            case "B1":
                return dwnldDir_Rowan;
            case "C":
            case "C1":
                return dwnldDir_Rebecca;
            case "K":
                return dwnldDir_Notfowd;
            default:
                return null;  // Return null if internal team does not match expected values
        }
    }

    // Method to retrieve file extension dynamically
    public static String getFileExtension(String fileName, String directoryPath) {
        File directory = new File(directoryPath);
        if (directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                if (file.getName().startsWith(fileName)) {
                    return file.getName().substring(file.getName().lastIndexOf(".")); // Return the extension
                }
            }
        }
        return ""; // If no valid extension found, return empty
    }

    // Updated sendEmail method to handle attachment properly
    public static void sendEmail(String fromEmail, String password, String toEmail, String subject, String body, File attachment) {
        try {
            // Set up the email properties
            Properties properties = new Properties();
            properties.put("mail.smtp.host", "smtp.office365.com");
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");

            // Create session with authentication
            Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, password);
                }
            });

            // Create a new email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);

            // Create body part for the email text
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setText(body);

            // Create body part for the attachment
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(attachment);

            // Create a multipart message to hold both the text and the attachment
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);
            multipart.addBodyPart(attachmentPart);

            // Set the multipart message as the content of the email
            message.setContent(multipart);

            // Send the email
            Transport.send(message);
            System.out.println("Email sent successfully to " + toEmail + " with subject: " + subject);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
