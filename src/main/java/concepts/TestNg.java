package concepts;

import java.io.FileInputStream;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TestNg {

	WebDriver driver;
	String browser;
	String url;

	// Element list
	By USER_NAME_FIELD = By.xpath("//input[@id='username']");
	By PASSWORD_FIELD = By.xpath("//input[@id='password']");
	By LOGIN_BUTTON_FIELD = By.xpath("//button[contains(text(), 'Sign in')]");
	By DASHBOARD_HEADER_FIELD = By.xpath("//h2[contains(text(), ' Dashboard ')]");

	By CUSTOMER_MENU_BUTTON_FIELD = By.xpath("//span[contains(text(), 'Customers')]");
	By ADD_CUSTOMER_MENU_BUTTON_FIELD = By.xpath("//a[contains(text(), 'Add Customer')]");
	By FULL_NAME_FIELD = By.xpath("//input[@id='account']");
	By COMPANY_FIELD = By.xpath("//select[@id='cid']");
	By EMAIL_FIELD = By.xpath("//input[@id='email']");
	By COUNTRY_FIELD = By.xpath("//select[@id='country']");

	// Test data
	String userName = "demo@techfios.com";
	String password = "abc123";
	String dashboardHeader = "Dashboard";

	@BeforeClass
	public void readConfig() {

		// InputStream //BufferedReader //Scanner //FileReader (classes available for read)

		try {

			InputStream input = new FileInputStream("src\\main\\java\\config\\config.properties");
			Properties prop = new Properties();
			prop.load(input);
			browser = prop.getProperty("browser");
			url = prop.getProperty("url");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@BeforeMethod
	public void init() {

		if (browser.equalsIgnoreCase("Chrome")) {
			driver = new ChromeDriver();
		} else if (browser.equalsIgnoreCase("Firefox")) {
			driver = new FirefoxDriver();
		}

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		driver.manage().deleteAllCookies();
		driver.get(url);
	}

//	@Test(priority = 1)
	public void loginTest() {

		driver.findElement(USER_NAME_FIELD).sendKeys(userName);
		driver.findElement(PASSWORD_FIELD).sendKeys(password);
		driver.findElement(LOGIN_BUTTON_FIELD).click();
		Assert.assertEquals(driver.findElement(DASHBOARD_HEADER_FIELD).getText(), dashboardHeader,"Dashboard is not available.");
	}

	@Test(priority = 2)
	public void addCustomers() throws InterruptedException {
		
		loginTest();
		Thread.sleep(3000);
		driver.findElement(CUSTOMER_MENU_BUTTON_FIELD).click();
		driver.findElement(ADD_CUSTOMER_MENU_BUTTON_FIELD).click();
		Thread.sleep(3000);
		boolean fullNameField = driver.findElement(FULL_NAME_FIELD).isDisplayed();
		Assert.assertTrue(fullNameField, "Add customer page is not available");

		driver.findElement(FULL_NAME_FIELD).sendKeys("Selenium" + generateRandomNo(999)); 
		selectFromDropdown(COMPANY_FIELD, "Techfios");
		driver.findElement(EMAIL_FIELD).sendKeys("demo" + generateRandomNo(9999) + "@techfios.com"); 
		selectFromDropdown(COUNTRY_FIELD, "Afghanistan");

	}

	public int generateRandomNo(int boundryNo) {
		Random rnd = new Random();
		int randomNumber = rnd.nextInt(boundryNo);
		return randomNumber;
	}

	public void selectFromDropdown(By byLocator, String visibleText) {
		Select sel1 = new Select(driver.findElement(byLocator));
		sel1.selectByVisibleText(visibleText);
	}

	// @AfterMethod
	public void tearDown() {
		driver.close();
		driver.quit();
	}

}
