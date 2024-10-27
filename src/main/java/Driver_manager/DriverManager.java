package Driver_manager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import com.asis.util.MainClass;

import java.io.File;

public class DriverManager {

    private static WebDriver driver;

    public static void setDriver(String browser) {
        // Get the Jenkins workspace directory
        String downloadPath = MainClass.downloadDir;
        //System.getenv("WORKSPACE") + "/downloads";
       

        if (browser.equalsIgnoreCase("Chrome")) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");

            // Set download path for Chrome
            options.setExperimentalOption("prefs", new java.util.HashMap<String, Object>() {{
                put("download.default_directory", downloadPath);
                put("download.prompt_for_download", false);
                put("download.directory_upgrade", true);
                put("safebrowsing.enabled", true);
            }});

            driver = new ChromeDriver(options);

        } else if (browser.equalsIgnoreCase("Firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--headless");
            options.addArguments("--disable-gpu");

            // Set download path for Firefox
            options.addPreference("browser.download.dir", downloadPath);
            options.addPreference("browser.download.folderList", 2); // 2 means use custom download directory
            options.addPreference("browser.helperApps.neverAsk.saveToDisk", "application/pdf,application/octet-stream");

            driver = new FirefoxDriver(options);
        }

        // Ensure the download directory exists
        new File(downloadPath).mkdirs();
    }

    public static WebDriver getDriver() {
        return driver;
    }
}
