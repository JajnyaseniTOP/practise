package Driver_manager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.io.File;
import java.util.*;

public class DriverManager {

	public static WebDriver driver;

	public static void setDriver(String browser) {
		String workspacePath ="C:" + File.separator + "ATO Email files";
		if(browser == "Chrome") {
			Map<String, Object> prefs = new HashMap<>();
			prefs.put("download.default_directory", workspacePath); 
			ChromeOptions options = new ChromeOptions(); 
			options.setExperimentalOption("prefs", prefs); 
//			options.addArguments("--headless=new");
//			options.addArguments("--no-sandbox");
			driver = new ChromeDriver(options);

		}else {
			Map<String, Object> prefs = new HashMap<>();
			prefs.put("download.default_directory", workspacePath); 
			FirefoxOptions options = new FirefoxOptions();
//			options.addArguments("--headless=new");
//			options.addArguments("--no-sandbox");
			driver = new FirefoxDriver(options);
		}
	}

	public static WebDriver getDriver() {
		return driver;
	}
}