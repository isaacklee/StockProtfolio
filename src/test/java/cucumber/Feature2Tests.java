package cucumber;

import static org.junit.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * Step definitions for Cucumber tests.
 */
public class Feature2Tests {

//	// Scenario: Adding a stock to the portfolio without a sell date
	@Given("I am logged in to my account")
	public void i_am_logged_in_to_my_account() {
		RunCucumberTests.driver.get("https://localhost:8443/login.jsp");
	    try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    WebElement username = RunCucumberTests.driver.findElement(By.id("username"));
		username.sendKeys("asdf");
		WebElement password = RunCucumberTests.driver.findElement(By.id("password"));
		password.sendKeys("asdfasdf");
		WebElement loginButton = RunCucumberTests.driver.findElement(By.xpath("//*[@id=\"inner\"]/input[3]"));
		loginButton.click();
	}

	@When("I click on the {string} button")
	public void i_click_on_the_button(String string) {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    WebElement addStockButton = RunCucumberTests.driver.findElement(By.id(string));
	    addStockButton.click();
	}

	@When("I enter {string} into the Stock Ticker input")
	public void i_enter_into_the_Stock_Ticker_input(String string) {
	    WebElement stockTickerInput = RunCucumberTests.driver.findElement(By.id("stockTicker"));
	    stockTickerInput.sendKeys(string);
	}

	@When("I enter {string} into the number of shares input")
	public void i_enter_into_the_number_of_shares_input(String string) {
	    WebElement numOfSharesInput = RunCucumberTests.driver.findElement(By.id("numOfShares"));
	    numOfSharesInput.sendKeys(string);
	}

	@When("I enter {string} into the Date Bought input")
	public void i_enter_into_the_Date_Bought_input(String string) {
	    WebElement dateBoughtInput = RunCucumberTests.driver.findElement(By.id("dateBought"));
	    dateBoughtInput.click();
	    try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    System.out.println(string);
	    dateBoughtInput.sendKeys(string);
	}

	@When("I click the Add Stock button")
	public void i_click_the_Add_Stock_button() {
	    WebElement button = RunCucumberTests.driver.findElement(By.xpath("/html/body/div[1]/div[2]/div/form/div[5]/button[1]"));
	    button.click();
	}
	
	@Then("the {string} font element will be empty")
	public void the_font_element_will_be_empty(String string) {
		try {
		Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    WebElement fontElement = RunCucumberTests.driver.findElement(By.id(string));
	    String text = fontElement.getText();
	    assertEquals("", text);
	}
	
	//Scenario: Removing a stock from portfolio
	int numOfStocks = 0;
	@When("I click on a remove button for a portfolio stock")
	public void i_click_on_remove_button_for_a_portfolio_stock()
	{
		try {
			Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
		JavascriptExecutor js = (JavascriptExecutor) RunCucumberTests.driver;  
		js.executeScript("window.scrollBy(0,2000)");
		
		numOfStocks = RunCucumberTests.driver.findElements(By.className("track-portfolio-input")).size();
		
		WebElement removeStockButton = RunCucumberTests.driver.findElement(By.xpath("//*[@id=\"portfolio-table\"]/div[4]/div[2]/button"));
		removeStockButton.click();
	}
	
	@When("I click on the Delete Stock button")
	public void i_click_on_the_delete_stock_button()
	{
		try {
			Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
		WebElement removeButton = RunCucumberTests.driver.findElement(By.xpath("//*[@id=\"sell-modal-content\"]/div[2]/input"));
		removeButton.click();
	}
	
	@Then("that stock will not appear in the portfolio")
	public void that_stock_will_not_appear_in_the_portfolio()
	{
		try {
			Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
		assertEquals(numOfStocks-1, RunCucumberTests.driver.findElements(By.className("track-portfolio-input")).size());
	}
	
	
	// Scenario: Adding a stock to the portfolio with a sell date
	@When("I enter {string} into the Date Sold input")
	public void i_enter_into_the_Date_Sold_input(String string) {
		WebElement dateSoldInput = RunCucumberTests.driver.findElement(By.id("dateSold"));
	    dateSoldInput.click();
	    try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    System.out.println(string);
	    dateSoldInput.sendKeys(string);
	}
	
	// Scenario: Adding a stock to the portfolio with a sell date before the purchase date
	@Then("the {string} font element will display {string}")
	public void the_font_element_will_display(String string, String string2) {
		try {
		Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    WebElement fontElement = RunCucumberTests.driver.findElement(By.id(string));
	    String text = fontElement.getText();
	    assertEquals(string2, text);
	}
	
	// Scenario: Adding a stock to the portfolio with a ticker that doesn't exist
	// good
	
	// Scenario: Adding a stock to the portfolio with an improper number of shares
	// good
		
	// Scenario: Adding a stock to the portfolio via CSV upload
	
	
	
	@Before()
	public void before() {
		// allow insecure certificates
		ChromeOptions chromeOptions = new ChromeOptions();
		 chromeOptions.setCapability("acceptInsecureCerts", true);
		RunCucumberTests.driver = new ChromeDriver(chromeOptions);
	}
	
	@After()
	public void after() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		RunCucumberTests.driver.quit();
	}

}
