package Driver_manager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class DriverManager {
	
	public static WebDriver driver;
	
	public static void setDriver(String browser) {
		if(browser == "Chrome") {
			driver = new ChromeDriver();
		}else {
			driver = new FirefoxDriver();
		}
	}
	
	public static WebDriver getDriver() {
		return driver;
	}
}