package cucumber;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Step definitions for Cucumber tests.
 */
public class Feature5Tests {
	private static final String ROOT_URL = "https://localhost:8443/";
	
	Map<String, String> mobileEmulation = new HashMap<>();

	@Given("I am on a mobile device")
	public void i_am_on_a_mobile_device() {
		
		mobileEmulation.put("deviceName", "iPhone X");
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
		RunCucumberTests.driver =  new ChromeDriver(chromeOptions);
		
	}

	@When("I am logged in")
	public void i_am_logged_in() {
		RunCucumberTests.driver.get(ROOT_URL + "login.jsp");
		WebElement username = RunCucumberTests.driver.findElement(By.id("username"));
		username.sendKeys("asdf");
		WebElement password = RunCucumberTests.driver.findElement(By.id("password"));
		password.sendKeys("asdfasdf");
		WebElement loginButton = RunCucumberTests.driver.findElement(By.xpath("//*[@id=\"inner\"]/input[3]"));
		loginButton.click();
	}


	@Then("I will see that certain buttons have smaller fonts")
	public void i_will_see_that_certain_buttons_have_smaller_fonts() {
		RunCucumberTests.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		WebElement buttonFont = RunCucumberTests.driver.findElement(By.id("import-csv-button"));
		String font = buttonFont.getCssValue("font-size");
		System.out.println("val: " + font);
		assertEquals("14.4px", font);
	}
	
}


