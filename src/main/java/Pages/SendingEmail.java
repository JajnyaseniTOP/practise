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

public class SendingEmail {

    public static void main(String[] args) {
        String excelFilePath = "C:\\Users\\Jajnyaseni TOP\\Documents\\MyOwnTesting.xlsx";
        String downloadDir = "C:\\Users\\Jajnyaseni TOP\\Downloads\\testdownload";
        String fromEmail = "toptechautomation@theoutsourcepro.com.au";
        String password = "J7OJb*ZwQD25HpC2KO8*n";
        String body = "This is a default email body."; 

        try {
            FileInputStream file = new FileInputStream(excelFilePath);
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                Cell emailCell = row.getCell(6);   
                Cell fileCell = row.getCell(7);   

                if (emailCell != null && fileCell != null) {
                    String recipientEmail = emailCell.getStringCellValue();
                    String fileName = fileCell.getStringCellValue();
                    File attachment = new File(downloadDir, fileName);

                    // Validate email format
                    if (validateEmail(recipientEmail)) {
                        // Send email if valid
                        sendEmail(fromEmail, password, recipientEmail, fileName, body, attachment);
                    } else {
                        System.out.println("Invalid email format: " + recipientEmail + ". Skipping...");
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

    // Method to send email
    public static void sendEmail(String fromEmail, String password, String toEmail, String subject, String body, File attachment) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.office365.com"); 
		properties.put("mail.smtp.port", "587"); 
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true"); 

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);  // Use file name from Column 6 as the subject

            // Set the email body
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(body, "text/plain");

            // Attachment part
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(attachment);

            // Combine parts
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);

            // Send email
            Transport.send(message);
            System.out.println("Email sent successfully to " + toEmail + " with subject: " + subject);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

