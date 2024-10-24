package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.asis.util.ClientExcel;
import com.asis.util.MainClass;
import Driver_manager.DriverManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ATOcommHistoryExtarctionPage extends MainClass {
    private static final Logger LOGGER = Logger.getLogger(ATOcommHistoryExtarctionPage.class.getName());
    ClientExcel creator = new ClientExcel();
    private String downloadDir; // Directory where files are downloaded

    @FindBy(xpath = "//button[@title='Next page']")
    private WebElement next;

    @FindBy(xpath = "//button[@title='Download']")
    private WebElement download;

    @FindBy(xpath = "//button[contains(text(),'Yes')]")
    private WebElement yesPopUp;

    @FindBy(xpath = "//tbody[@data-bind=\"css: { 'rowgroup': rowGroup.header }\"]//tr//td//a")
    private List<WebElement> links;  
    
    @FindBy(xpath = "//tbody/tr[@class=\"table-row\"]")
    private List<WebElement> commTableHistory;
    
    @FindBy(xpath = "//th[@data-header='Name']")
    private List<WebElement> clientName;

    public ATOcommHistoryExtarctionPage() {
        PageFactory.initElements(DriverManager.getDriver(), this);
        setDownloadDirectory();
        LOGGER.info("Download Directory set to: " + downloadDir);
    }

    private void setDownloadDirectory() {
        if (isServerEnvironment()) {
            this.downloadDir = "/var/lib/jenkins/workspace/ATOEmail/downloads";
        } else {
            this.downloadDir = System.getProperty("user.home") + "/Downloads";
        }
        
        // Create directory if it doesn't exist
        File dir = new File(downloadDir);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (created) {
                LOGGER.info("Created download directory: " + downloadDir);
            } else {
                LOGGER.warning("Failed to create download directory: " + downloadDir);
            }
        }
    }

    public void clickDownloadButton() throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(download));
        LOGGER.info("Clicking download button");
        Thread.sleep(5000);  // Adjusted for server performance
        download.click();
        LOGGER.info("Download button clicked");
    }

    public void clickPopUp() {
        wait.until(ExpectedConditions.elementToBeClickable(yesPopUp));
        LOGGER.info("Clicking popup");
        yesPopUp.click();
        LOGGER.info("Popup clicked");
    }

    public ArrayList<ArrayList<String>> extractCommTableStatement() throws InterruptedException {
        creator.createEmptyExcelSheet(); 
        Thread.sleep(5000);
        LOGGER.info("Extracting communication table statement");
        for (WebElement tr : commTableHistory) {
            if (tr.isDisplayed()) {
                List<WebElement> tdData = tr.findElements(By.xpath(".//td | .//th"));
                ArrayList<String> tdRowData = new ArrayList<>();

                for (WebElement td : tdData) {
                    tdRowData.add(td.getText());
                }

                ACTIVITY_STATEMENT_DATA.add(tdRowData);
            }
        }
        ClientExcel.writeDataToExcel(ACTIVITY_STATEMENT_DATA);
        LOGGER.info("Data extracted and written to Excel");
        return ACTIVITY_STATEMENT_DATA;
    }

    public void clickAllLinks() throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOfAllElements(links));
        LOGGER.info("Clicking all links");

        for (WebElement link : links) {
            wait.until(ExpectedConditions.elementToBeClickable(link));
            Thread.sleep(3000);

            LOGGER.info("Clicking link: " + link.getText());
            link.click(); 
            Thread.sleep(5000);
            printLatestDownloadedFileName();
        }
    }

    public void printLatestDownloadedFileName() {
        File dir = new File(downloadDir);
        File[] files = dir.listFiles();

        if (files != null && files.length > 0) {
            File latestFile = files[0];
            for (File file : files) {
                if (file.lastModified() > latestFile.lastModified()) {
                    latestFile = file;
                }
            }
            String name = latestFile.getName();
            LOGGER.info("Latest downloaded file: " + name);
            ClientExcel.addPdfName(name);
        } else {
            LOGGER.warning("No files found in directory: " + downloadDir);
        }
    }

    public void waitForFileDownload(String fileName, int timeout) throws InterruptedException {
        File dir = new File(downloadDir);
        int waited = 0;
        while (waited < timeout) {
            File[] files = dir.listFiles((d, name) -> name.equals(fileName));
            if (files != null && files.length > 0) {
                LOGGER.info("File downloaded: " + fileName);
                break;
            }
            Thread.sleep(1000);
            waited += 1000;
        }
        if (waited >= timeout) {
            LOGGER.warning("Timeout waiting for file download: " + fileName);
        }
    }

    public void closeBrowser() {
        LOGGER.info("Closing browser");
        DriverManager.getDriver().quit();
    }

    private boolean isServerEnvironment() {
        String env = System.getenv("ENVIRONMENT");
        LOGGER.info("Current environment: " + (env != null ? env : "Not set"));
        return "server".equals(env);
    }

    // Additional method to check download directory contents
    public void checkDownloadDirectory() {
        File dir = new File(downloadDir);
        File[] files = dir.listFiles();
        if (files != null) {
            LOGGER.info("Files in download directory:");
            for (File file : files) {
                LOGGER.info(file.getName());
            }
        } else {
            LOGGER.warning("Unable to list files in download directory or directory is empty");
        }
    }
}