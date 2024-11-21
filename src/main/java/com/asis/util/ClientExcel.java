package com.asis.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

public class ClientExcel extends MainClass{

	private static Workbook workbook;
	private static Sheet sheet;
	private static int currentRowNum = 1;
	private static int currentRowNum2 = 1;
	private static int currentRowNum3 = 1;

	/*====================Creation of Empty Excel Sheet===================================*/

	public void createEmptyExcelSheet() {
		workbook = new HSSFWorkbook();
		sheet = workbook.createSheet("Client Data");

		String[] headers = { "Name", "Client ID", "Subject", "Channel", "Issue Date", "Client Code", "Client Email ID", "File Name", "PDF File" };

		Row headerRow = sheet.createRow(0); 
		for (int i = 0; i < headers.length; i++) {
			Cell cell = headerRow.createCell(i);  
			cell.setCellValue(headers[i]); 
		}
	}

	/*====================Table Extraction and Putting data into Excel===================================*/

	public static ArrayList<ArrayList<String>> writeDataToExcel(ArrayList<ArrayList<String>> data) {
		if (sheet == null) {
			return data;
		}
		int rowNum = 1;
		for (ArrayList<String> rowData : data) {
			Row row = sheet.createRow(rowNum++);
			int colNum = 0;
			for (String cellData : rowData) {
				Cell cell = row.createCell(colNum++);
				cell.setCellValue(cellData);
			}
		}
		saveExcelFile();
		return data;
	}
	/*====================Read Of First Column===================================*/

	public static ArrayList<String> readFirstColumn(String filePath) {
		ArrayList<String> firstColumnData = new ArrayList<>();

		try (FileInputStream fis = new FileInputStream(new File(filePath));
				Workbook workbook = WorkbookFactory.create(fis)) {

			Sheet sheet = workbook.getSheetAt(0);
			for (Row row : sheet) {
				Cell cell = row.getCell(0); 
				if (cell != null && cell.getCellType() == CellType.STRING) {
					firstColumnData.add(cell.getStringCellValue());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		firstColumnData.remove(0);
		System.out.println("First colm data " + firstColumnData.size());
		
		return firstColumnData;
	} 

	/*====================Removal of Last character of client name ===================================*/

	public static void clientNamesRemoval() {
		String filePath = "ClientData.xls"; 
		firstColumn = readFirstColumn(filePath); 

		for (int cnt = 0; cnt < firstColumn.size(); cnt++) {
			String clientName = firstColumn.get(cnt).trim(); 

			if (cnt >= 0) {
				int length = clientName.length();
				if (length > 2 && clientName.charAt(length - 2) == ' ' 
						&& Character.isLetter(clientName.charAt(length - 1))) {
					clientName = clientName.substring(0, length - 2);
				}
				else if(length<=2) {
					clientName = clientName.substring(0);
					System.out.println("this is a two_charcter_string"+ clientName);
				}
				else {
					clientName = clientName.substring(0, length - 1);
					System.out.println("this is eleminated string "+ clientName);
				}
				clientName = formatCommaSeparatedName(clientName);
				clientName = capitalizeName(clientName);
				
				clientNames.add(clientName); 
				saveExcelFile();
			}
			
		}
//		clientNames.remove(0);
		System.out.println("First colm " + firstColumn.size());
		System.out.println("Client name size" + clientNames.size());
//		clientNames.remove(0);
	}

	/*====================Formating of the client name data===================================*/

	private static String formatCommaSeparatedName(String name) {
		if (name.contains(",")) {
			name = name.replaceAll(",(\\S)", ", $1");
		}
		return name;
	}

	/*====================Changing the client name into capital form===================================*/

	private static String capitalizeName(String name) {
		if (name == null || name.isEmpty()) {
			return name; 
		}
		String[] words = name.split(" ");
		StringBuilder capitalized = new StringBuilder();
		for (String word : words) {
			if (!word.isEmpty()) {
				String capitalizedWord = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
				capitalized.append(capitalizedWord).append(" ");
			}
		}
		return capitalized.toString().trim();
	}

	/*====================Adding the client code and email into excel sheet===================================*/

	public static void addClientData(String clientCode, String clientEmail) {
		if (sheet != null) {
			Row row = sheet.getRow(currentRowNum);
			if (row == null) {
				row = sheet.createRow(currentRowNum);
			}

			Cell codeCell = row.createCell(5); 
			Cell emailCell = row.createCell(6); 
			codeCell.setCellValue(clientCode); 
			emailCell.setCellValue(clientEmail); 

			currentRowNum++;
		}
	}

	/*====================Saving the Excel===================================*/

	public static void saveExcelFile() {
		String fileName = "ClientData.xls";
		File file = new File(fileName);

		try (FileOutputStream fileOut = new FileOutputStream(file)) {
			workbook.write(fileOut);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/*====================Writing the Combined data of client name and client code into column of excel===================================*/

	public static void writeCombinedDataToExcel(String clientName, String clientCode) {
		if (sheet != null) {
			Row row = sheet.getRow(currentRowNum2); 
			if (row == null) {
				row = sheet.createRow(currentRowNum2);
			}

			String combinedData = clientName + " - " + clientCode;

			Cell combinedCell = row.createCell(7);
			combinedCell.setCellValue(combinedData);
			saveExcelFile();
			currentRowNum2++;
		}
	}

	/*====================Adding the PDF name into excel sheet===================================*/

	public static void addPdfName(String name) {
		if (sheet != null) {
			Row row = sheet.getRow(currentRowNum3);
			if (row == null) {
				row = sheet.createRow(currentRowNum3);
			}


			Cell codeCell = row.createCell(8); 
			codeCell.setCellValue(name); 
			saveExcelFile();
			currentRowNum3++;
		}
	}

	/*====================Read Of Subject Column===================================*/

	public static ArrayList<String> readSubjectColumn(String filePath) {
		ArrayList<String> subjectColumnData = new ArrayList<>();

		try (FileInputStream fis = new FileInputStream(new File(filePath));
				Workbook workbook = WorkbookFactory.create(fis)) {

			Sheet sheet = workbook.getSheetAt(0);
			for (Row row : sheet) {
				Cell cell = row.getCell(2); 
				if (cell != null && cell.getCellType() == CellType.STRING) {
					String subject = cell.getStringCellValue();
					subject = replaceSpecialCharacters(subject);
					subjectColumnData.add(subject);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		subjectColumnData.remove(0); 
		System.out.println("Subject colm " + subjectColumnData.size());
//		subjectColumnData.remove(0); 
		return subjectColumnData;
	}

	/*====================Replace special character===================================*/

	private static String replaceSpecialCharacters(String subject) {
		if (subject.contains("/") || subject.contains("\\")) {
			subject = subject.replace("/", "or").replace("\\", "or");
		}
		return subject;
	}


	/*====================Read PDF File Names from Column 7===================================*/

	public static ArrayList<String> readFileNamesFromColumn7(String filePath) {
		ArrayList<String> fileNamesColumn7 = new ArrayList<>();
		HashSet<String> uniqueFileNames = new HashSet<>();

		try (FileInputStream fis = new FileInputStream(new File(filePath));
				Workbook workbook = WorkbookFactory.create(fis)) {

			Sheet sheet = workbook.getSheetAt(0);
			for (Row row : sheet) {
				Cell cell = row.getCell(7); 
				if (cell != null && cell.getCellType() == CellType.STRING) {
					String fileName = cell.getStringCellValue().trim(); 
					String originalFileName = fileName;

					int count = 1;
					while (uniqueFileNames.contains(fileName)) {
						fileName = originalFileName + "_new" +count ;
						count++;
					}

					fileNamesColumn7.add(fileName);
					uniqueFileNames.add(fileName);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		fileNamesColumn7.remove(0);	    
		return fileNamesColumn7;
	}


	/*====================Renaming the PDF file===================================*/

	public static void renamePdfFilesInDownloads(String downloadDir) {
		ArrayList<String> pdfFileNames = ClientExcel.readPdfFileNamesFromColumn8(filePath);
		ArrayList<String> fileNamesColumn7 = ClientExcel.readFileNamesFromColumn7(filePath); 

		if (pdfFileNames.size() != fileNamesColumn7.size()) {
			return;
		}

		int cnt = 0;
		for (String pdfFileName : pdfFileNames) {
			String fullPath = downloadDir + File.separator + pdfFileName.trim();
			File pdfFile = new File(fullPath);

			if (pdfFile.exists()) {
				System.out.println("Found: " + pdfFileName);

				String currentExtension = getFileExtension(pdfFile);

				if (cnt < fileNamesColumn7.size()) {
					String newFilePath = downloadDir + File.separator + fileNamesColumn7.get(cnt) + "." + currentExtension;
					System.out.println(newFilePath);
					File renamedFile = new File(newFilePath);
					System.out.println(Objects.isNull(renamedFile));
					if (pdfFile.renameTo(renamedFile)) {
						System.out.println("Renamed " + pdfFileName + " to " + fileNamesColumn7.get(cnt) + "." + currentExtension);
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

	/*====================Check the Extension===================================*/

	private  static String getFileExtension(File file) {
		String fileName = file.getName();
		int dotIndex = fileName.lastIndexOf('.');
		if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
			return fileName.substring(dotIndex + 1); 
		} else {
			return ""; 
		}
	}

	/*====================Read PDF File Names from Column 8===================================*/

	public static ArrayList<String> readPdfFileNamesFromColumn8(String filePath) {
		ArrayList<String> pdfFileNames = new ArrayList<>();

		try (FileInputStream fis = new FileInputStream(new File(filePath));
				Workbook workbook = WorkbookFactory.create(fis)) {

			Sheet sheet = workbook.getSheetAt(0);
			for (Row row : sheet) {
				Cell cell = row.getCell(8); 
				if (cell != null && cell.getCellType() == CellType.STRING) {
					pdfFileNames.add(cell.getStringCellValue());
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		pdfFileNames.remove(0);

		return pdfFileNames;
	}

	/*====================Check if NOA exist for that client===================================*/

	public static void checkNoticeOfAssessment(String filePath, String downloadDir) {
		ArrayList<String> subjectColumnData = ClientExcel.readSubjectColumn(filePath);  
		boolean found = false;  

		ArrayList<String> firstColumnData = ClientExcel.readFirstColumn(filePath);

		for (int i = 0; i < subjectColumnData.size(); i++) {
			String subject = subjectColumnData.get(i).trim();

			if (subject.toLowerCase().startsWith("notice of assessment")) {
				String correspondingValue = ClientExcel.readPdfFileNamesFromColumn8(filePath).get(i+1).trim();

				String cellFromColumn0 = firstColumnData.get(i).trim();

				searchPdfFilesInDownloads1(filePath, downloadDir, correspondingValue);

				found = true;  
			}
		}

		if (!found) {
		}
	}

	/*====================Search PDF Files in Downloads===================================*/

	public static void searchPdfFilesInDownloads1(String filePath, String downloadDir, String pdfFileName) {
		String fullPath = downloadDir + File.separator + pdfFileName;

		File pdfFile = new File(fullPath);
		if (pdfFile.exists()) {
			readPdfFile(fullPath);
		} else {
			System.out.println("Not Found: " + pdfFileName);
		}
	}

	/*====================Read PDF from Downloads===================================*/

	public static void readPdfFile(String pdfFilePath) {
		File pdfFile = new File(pdfFilePath);
		if (pdfFilePath.toLowerCase().endsWith(".html")) {
			System.out.println("Found HTML file. Skipping: " + pdfFilePath);
			return;
		}	   
		try (PDDocument document = PDDocument.load(pdfFile)) {
			if (!document.isEncrypted()) {
				PDFTextStripper pdfStripper = new PDFTextStripper();
				String pdfText = pdfStripper.getText(document);

				HashMap<String, String> extractedData = new HashMap<>();

				Pattern datePattern = Pattern.compile("Date of issue\\s*(\\d{2} \\w+ \\d{4})");
				Matcher dateMatcher = datePattern.matcher(pdfText);
				if (dateMatcher.find()) {
					String dateOfIssue = dateMatcher.group(1);
					extractedData.put("Date of Issue", dateOfIssue);
				}

				Pattern refPattern = Pattern.compile("Our reference\\s*(\\d{3} \\d{3} \\d{3} \\d{4})");
				Matcher refMatcher = refPattern.matcher(pdfText);
				if (refMatcher.find()) {
					String referenceNumber = refMatcher.group(1);
					extractedData.put("Reference Number", referenceNumber);
				}

				Pattern incomePattern = Pattern.compile("Your taxable income is \\$([\\d,]+)");
				Matcher incomeMatcher = incomePattern.matcher(pdfText);
				if (incomeMatcher.find()) {
					String taxableIncome = incomeMatcher.group(1).replace(",", "");
					extractedData.put("Taxable Income", taxableIncome);
				}

				Pattern resultPattern = Pattern.compile("Result of this notice\\s+(\\S+ \\S+)");
				Matcher resultMatcher = resultPattern.matcher(pdfText);
				if (resultMatcher.find()) {
					String resultAmount = resultMatcher.group(1);
					extractedData.put("Result", resultAmount );
				}
				else {
					extractedData.put("Result of Notice", "0.0");
				}
				System.out.println("Extracted Data: " + extractedData);

			} else {
				System.out.println("The PDF is encrypted. Cannot read.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}