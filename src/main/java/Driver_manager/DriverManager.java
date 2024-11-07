package Driver_manager;
 
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
 
public class DriverManager {
	
	public static WebDriver driver;
	
	public static void setDriver(String browser) {
		if(browser == "Chrome") {
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--headless=new");
			options.addArguments("--no-sandbox");
			driver = new ChromeDriver(options);
			
		}else {
			FirefoxOptions options = new FirefoxOptions();
			options.addArguments("--headless");
			options.addArguments("--disable-gpu");
			driver = new FirefoxDriver(options);
		}
	}
	
	public static WebDriver getDriver() {
		return driver;
	}
}