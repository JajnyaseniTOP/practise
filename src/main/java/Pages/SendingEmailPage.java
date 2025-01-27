package Pages;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SendingEmailPage {
    public static void main(String[] args) {
        String excelFilePath = "C:\\Users\\TOP\\Documents\\TestingDocuments.xlsx";
        String dwnldDir_Lindy = "C:/ATO Email files/Email_Files_2025-01-07/K-Lindy";
        String dwnldDir_Rowan = "C:/ATO Email files/Email_Files_2025-01-07/B-Rowan";
        String dwnldDir_Rebecca = "C:/ATO Email files/Email_Files_2025-01-07/C-Rebecca";
        String dwnldDir_Slain = "C:/ATO Email files/Email_Files_2025-01-07/A-Sian";
        String dwnldDir_Melvin = "C:/ATO Email files/Email_Files_2025-01-07/D-Melvin";

        String fromEmail = "toptechautomation@theoutsourcepro.com.au";
        String password = "J7OJb*ZwQD25HpC2KO8*n";

        // Define the list of specific content to check
        List<String> validContents = Arrays.asList(
                "Notification of a mistake in your income tax return",
                "Penalty warning - Lodgment",
                "Lodgment-Overdue-Final warning",
                "Taxable payments annual report - Second reminder to lodge",
                "ABN - Application refused",
                "ABN - Cancellation advice",
                "Debt - Referral notification - Debt collection agency",
                "Registration confirmation - Fuel tax credit",
                "Lodgment-Overdue-Lodge now",
                "ABN - Registered, replaced or reinstated",
                "Failure to lodge Activity Statement or GST Payment Slip",
                "Penalty notification - Failure to lodge",
                "Lodgment-Overdue-Reminder",
                "Confirming your payment plan"
        );

        try {
            FileInputStream file = new FileInputStream(excelFilePath);
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                Cell emailCell = row.getCell(6);   // Column 6 for Email ID
                Cell fileCell = row.getCell(7);    // Column 7 for the file name (subject and attachment)
                Cell internalTeamCell = row.getCell(9); // Column 9 for internal team info
                Cell nameCell = row.getCell(0);    // Column 0 for the name to replace
                Cell contentCell = row.getCell(2); // Column 2 for the content

                if (emailCell != null && fileCell != null && internalTeamCell != null && nameCell != null && contentCell != null) {
                    String recipientEmail = emailCell.getStringCellValue();
                    String fileName = fileCell.getStringCellValue();
                    String internalTeam = internalTeamCell.getStringCellValue().trim();
                    String recipientName = nameCell.getStringCellValue().trim();
                    String content = contentCell.getStringCellValue().trim();

                    if (validContents.contains(content)) {
                        // Determine CC email based on internal team
                        String ccEmail = null;
                        switch (internalTeam) {
                            case "A":
                            case "A1":
                                ccEmail = "asis.kaur@theoutsourcepro.com.au";
                                break;
                            case "B":
                            case "B1":
                                ccEmail = "narsingh@theoutsourcepro.com.au";
                                break;
                            case "C":
                            case "C1":
                                ccEmail = "vinod.gaddirala@theoutsourcepro.com.au";
                                break;
                            case "K":
                                ccEmail = "jajnyaseni.swain@theoutsourcepro.com.au";
                                break;
                            case "D":
                                ccEmail = "jajnyaseni.swain@theoutsourcepro.com.au";
                                break;
                            default:
                                ccEmail = null; // Ignore other internal team names
                                break;
                        }

                        // Extract the second word of the recipient's name
                        String[] nameParts = recipientName.split("\\s+");
                        String emailScndWrd = (nameParts.length > 1) ? nameParts[1] : nameParts[0];

                        String body = "Dear " + emailScndWrd + ",\n\n" +
                                "Hope you are well. Please see attached, correspondence from the Australian Taxation Office.\n\n" +
                                "It is important, you read it.\n\n" +
                                "1. If you have already actioned this or paid this account, please keep this letter only for your record.\n\n" +
                                "2. If an action needs to be taken, please take so in line with the letter attached.\n\n" +
                                "3. If you have any queries or need assistance, please email us at correspodence@fortunaadvisors.com.au or direct to your client manager.\n\n" +
                                "Kind Regards, \nNatalie Nicolaou\nAdministrator";

                        String downloadDir = getDownloadDir(internalTeam, dwnldDir_Slain, dwnldDir_Rowan, dwnldDir_Rebecca, dwnldDir_Lindy,dwnldDir_Melvin);
                        if (downloadDir != null) {
                            String fileExtension = getFileExtension(fileName, downloadDir);  // Get file extension dynamically
                            if (!fileExtension.isEmpty()) {
                                File attachment = new File(downloadDir, fileName + fileExtension);

                                // Check if the file exists before proceeding with email sending
                                if (attachment.exists()) {
                                    if (validateEmail(recipientEmail)) {
                                        sendEmail(fromEmail, password, recipientEmail, fileName, body, attachment, ccEmail);
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
            }
            workbook.close();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean validateEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static String getDownloadDir(String internalTeam, String dwnldDir_Slain, String dwnldDir_Rowan, String dwnldDir_Rebecca, String dwnldDir_Lindy,String dwnldDir_Melvin) {
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
                return dwnldDir_Lindy;
            case "D":
                return dwnldDir_Melvin;   
            default:
                return null;
        }
    }

    public static String getFileExtension(String fileName, String directoryPath) {
        File directory = new File(directoryPath);
        if (directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                if (file.getName().startsWith(fileName)) {
                    return file.getName().substring(file.getName().lastIndexOf("."));
                }
            }
        }
        return "";
    }

    public static void sendEmail(String fromEmail, String password, String toEmail, String subject, String body, File attachment, String ccEmail) {
        try {
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

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));

            if (ccEmail != null) {
                message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccEmail));
            }

            message.setSubject(subject);

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setText(body);

            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(attachment);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);

            Transport.send(message);
            System.out.println("Email sent successfully to " + toEmail + " with subject: " + subject);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}