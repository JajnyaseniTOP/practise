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

public class ATOcommHistoryExtarctionPage extends MainClass {
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
        setDownloadDirectory(); // Set the download directory based on environment
    }

    // Method to set download directory based on local/server environment
    private void setDownloadDirectory() {
        if (isServerEnvironment()) {
            // On the server (e.g., Jenkins), configure a specific path
            this.downloadDir = "/var/lib/jenkins/workspace/ATOEmail/downloads";
        } else {
            // Local system download path
            this.downloadDir = System.getProperty("user.home") + "/Downloads";
        }
    }

    public void clickDownloadButton() throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(download));
        Thread.sleep(5000);  // Adjusted for server performance
        download.click();
    }

    public void clickPopUp() {
        wait.until(ExpectedConditions.elementToBeClickable(yesPopUp));
        yesPopUp.click();
    }

    public ArrayList<ArrayList<String>> extractCommTableStatement() throws InterruptedException {
        creator.createEmptyExcelSheet(); 
        Thread.sleep(5000);
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
        return ACTIVITY_STATEMENT_DATA;
    }

    public void clickAllLinks() throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOfAllElements(links));

        for (WebElement link : links) {
            wait.until(ExpectedConditions.elementToBeClickable(link));
            Thread.sleep(3000);

            link.click(); 
            Thread.sleep(5000);
            printLatestDownloadedFileName();
        }
    }

    // Print the latest downloaded file name based on last modified date
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
            System.out.println("Latest file: " + name);  // Log file name for Jenkins visibility
            ClientExcel.addPdfName(name);
        } else {
            System.out.println("No files found in directory: " + downloadDir);  // Log for Jenkins
        }
    }

    // Method to wait for file download to complete
    public void waitForFileDownload(String fileName, int timeout) throws InterruptedException {
        File dir = new File(downloadDir);
        int waited = 0;
        while (waited < timeout) {
            File[] files = dir.listFiles((d, name) -> name.equals(fileName));
            if (files != null && files.length > 0) {
                break; // File found
            }
            Thread.sleep(1000); // Wait 1 second before checking again
            waited += 1000;
        }
    }

    public void closeBrowser() {
        DriverManager.getDriver().quit();
    }

    // This is a mock method to simulate checking the environment
    private boolean isServerEnvironment() {
        // Add your logic to check if this is running on a server, e.g., Jenkins environment
        String env = System.getenv("ENVIRONMENT");
        return "server".equals(env);
    }
}
