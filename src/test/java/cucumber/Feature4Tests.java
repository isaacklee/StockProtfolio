package cucumber;

import static org.junit.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class Feature4Tests {
	
	int oldLength;
	
	
	@When("I add stocks to the historical portfolio")
	public void i_add_stocks_to_the_historical_portfolio() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    WebElement addHistoricalButton = RunCucumberTests.driver.findElement(By.xpath("//*[@id=\"add-historical-button\"]"));
	    addHistoricalButton.click();
	    
	    WebElement stockTickerInput = RunCucumberTests.driver.findElement(By.id("historical-stock-ticker"));
	    stockTickerInput.sendKeys("AAPL");
	    
	    WebElement viewStockButton = RunCucumberTests.driver.findElement(By.id("view-stock-button"));
	    viewStockButton.click();
	}

	@When("I click select all portfolio stocks")
	public void i_click_select_all_portfolio_stocks() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    WebElement selectAllButton = RunCucumberTests.driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[3]/div[2]/div[3]/button[1]"));
	    selectAllButton.click();
	}

	@Then("all historical stocks will have their trackers toggled on")
	public void all_historical_stocks_will_have_their_trackers_toggled_on() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		int numActive = 0;
		
		for(int i = 0; i < RunCucumberTests.driver.findElements(By.className("track-historical-input")).size(); i++)
		{
			if(RunCucumberTests.driver.findElements(By.className("track-historical-input")).get(i).isSelected())
			{
				numActive++;
			}
		}
		
		assertEquals(numActive, RunCucumberTests.driver.findElements(By.className("track-historical-input")).size());
	}

	@When("I click deselect all historical stocks")
	public void i_click_deselect_all_historical_stocks() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    WebElement deselectAllButton = RunCucumberTests.driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[3]/div[2]/div[3]/button[2]"));
	    deselectAllButton.click();
	}

	@Then("no historical stocks will have their trackers toggled on")
	public void no_historical_stocks_will_have_their_trackers_toggled_on() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

			int numActive = 0;
			
			for(int i = 0; i < RunCucumberTests.driver.findElements(By.className("track-historical-input")).size(); i++)
			{
				if(RunCucumberTests.driver.findElements(By.className("track-historical-input")).get(i).isSelected())
				{
					numActive++;
				}
			}
			
			assertEquals(numActive, 0);
	}
	
	@When("I add a stock to the historical portfolio")
	public void i_add_a_stock_to_the_historical_portfolio() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    WebElement addHistoricalButton = RunCucumberTests.driver.findElement(By.xpath("//*[@id=\"add-historical-button\"]"));
	    addHistoricalButton.click();
	    
	    WebElement stockTickerInput = RunCucumberTests.driver.findElement(By.id("historical-stock-ticker"));
	    stockTickerInput.sendKeys("AAPL");
	    
	    WebElement viewStockButton = RunCucumberTests.driver.findElement(By.id("view-stock-button"));
	    viewStockButton.click();
	}

	@Then("I should see the stock I added in the historical portfolio")
	public void i_should_see_the_stock_I_added_in_the_historical_portfolio() {
		try {
			Thread.sleep(7000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		WebElement aaplTicker = RunCucumberTests.driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[3]/div[2]/div[5]/font"));
		assertEquals(aaplTicker.getText(), "AAPL");
	}

	@When("I remove the stock from the historical portfolio")
	public void i_remove_the_stock_from_the_historical_portfolio() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		oldLength = RunCucumberTests.driver.findElements(By.className("track-historical-input")).size();
		WebElement deleteTicker = RunCucumberTests.driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[3]/div[2]/div[5]/div[2]/button/i"));
		deleteTicker.click();
	}

	@Then("I should not see that stock in the historical portfolio")
	public void i_should_not_see_that_stock_in_the_historical_portfolio() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int length = RunCucumberTests.driver.findElements(By.className("track-historical-input")).size();
		assertEquals(oldLength - 1, length);
	}
	
	@When("I add an invalid {string} stock to the historical portfolio")
	public void i_add_an_invalid_stock_to_the_historical_portfolio(String string) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    WebElement addHistoricalButton = RunCucumberTests.driver.findElement(By.xpath("//*[@id=\"add-historical-button\"]"));
	    addHistoricalButton.click();
	    
	    WebElement stockTickerInput = RunCucumberTests.driver.findElement(By.id("historical-stock-ticker"));
	    stockTickerInput.sendKeys(string);
	    
	    WebElement viewStockButton = RunCucumberTests.driver.findElement(By.id("view-stock-button"));
	    viewStockButton.click();
	}

	@Then("I should see a {string} error")
	public void i_should_see_a_error(String string) {
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		WebElement error = RunCucumberTests.driver.findElement(By.id("historical-ticker-input-error"));
		assertEquals(error.getText(), string);
	}
	
	@When("I click on add historical stock button")
	public void i_click_on_add_historical_stock_button() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    WebElement addHistoricalButton = RunCucumberTests.driver.findElement(By.xpath("//*[@id=\"add-historical-button\"]"));
	    addHistoricalButton.click();
	}

	@Then("I should see a view stock button")
	public void i_should_see_a_view_stock_button() {
	    WebElement viewStockButton = RunCucumberTests.driver.findElement(By.id("view-stock-button"));
	    assertEquals(viewStockButton.getText(), "View Stock");
	}

	@Then("I should see a cancel button")
	public void i_should_see_a_cancel_button() {
	    WebElement cancelButton = RunCucumberTests.driver.findElement(By.xpath("/html/body/div[1]/div[3]/div/form/div[2]/button[2]"));
	    assertEquals(cancelButton.getText(), "Cancel");
	}
}
