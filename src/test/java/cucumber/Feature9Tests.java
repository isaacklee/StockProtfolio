package cucumber;

import static org.junit.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
/**
 * Step definitions for Cucumber tests.
 */
public class Feature9Tests {
	
	@Given("I am at {string}")
	public void i_am_at(String string) {
		RunCucumberTests.driver.get(string);
	}

	@Then("I will see a header which says {string}")
	public void i_will_see_a_header_which_says(String string) {
		WebElement header = RunCucumberTests.driver.findElement(By.cssSelector("#body > div.headercontainer > h3"));
		assertEquals(header.getText(), string);
	}

}
