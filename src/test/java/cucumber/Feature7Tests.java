package cucumber;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import static org.junit.Assert.assertEquals;

/**
 * Step definitions for Cucumber tests.
 */
public class Feature7Tests {
	private static final String ROOT_URL = "https://localhost:8443/";

	// scenario 1
	@Given("I am on the login page")
	public void i_am_on_the_login_page() {
		RunCucumberTests.driver.get(ROOT_URL + "login.jsp");
	}

	@When("I enter valid {string} into the username box")
	public void i_enter_valid_into_the_username_box(String string) {
		WebElement username = RunCucumberTests.driver.findElement(By.id("username"));
		username.sendKeys(string);
	}

	@When("I enter corresponding {string} into the password box")
	public void i_enter_corresponding_into_the_password_box(String string) {
		WebElement password = RunCucumberTests.driver.findElement(By.id("password"));
		password.sendKeys(string);
	}

	@When("I click the login button")
	public void i_click_the_login_button() {
		WebElement loginButton = RunCucumberTests.driver.findElement(By.xpath("//*[@id=\"inner\"]/input[3]"));
		loginButton.click();
	}

	@Then("I should be taken to the dashboard page")
	public void i_should_be_taken_to_the_dashboard_page() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String currURL = RunCucumberTests.driver.getCurrentUrl();
		assertEquals(ROOT_URL + "dashboard.jsp", currURL);
	}

	// login scenario 2
	@When("I enter the wrong password {string} into the password box")
	public void i_enter_the_wrong_password_into_the_password_box(String string) {
		WebElement password = RunCucumberTests.driver.findElement(By.id("password"));
		password.sendKeys(string);
	}

	@Then("I should get an entered username or password does not exist error")
	public void i_should_get_an_entered_username_or_password_does_not_exist_error() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		WebElement error = RunCucumberTests.driver.findElement(By.id("error"));
		String errorText = error.getText();
		assertEquals("The entered username/password does not exist", errorText);
	}
	
	@Given("I am on the signup page")
	public void i_am_on_the_signup_page() {
		RunCucumberTests.driver.get(ROOT_URL + "signup.jsp");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@When("I enter {string} into the username box")
	public void i_enter_into_the_username_box(String string) {
		WebElement username = RunCucumberTests.driver.findElement(By.xpath("//*[@id=\"username\"]"));
		username.sendKeys(string);
	}

	@When("I enter a password {string}")
	public void i_enter_a_password(String string) {
		WebElement password = RunCucumberTests.driver.findElement(By.id("password"));
		password.sendKeys(string);
	}

	// scenario 1
	@When("I confirm a different password {string}")
	public void i_confirm_a_different_password(String string) {
		WebElement confirmpassword = RunCucumberTests.driver.findElement(By.id("confirmpassword"));
		confirmpassword.sendKeys(string);
	}
	
	@When("I click the signup button")
	public void i_click_the_signup_button() {
		WebElement signupButton = RunCucumberTests.driver.findElement(By.xpath("//*[@id=\"inner\"]/input[4]"));
		signupButton.click();
	}

	@Then("I should get a passwords do not match error")
	public void i_should_get_a_passwords_do_not_match_error() {
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		WebElement error = RunCucumberTests.driver.findElement(By.id("passwordError"));
		String errorText = error.getText();
		assertEquals("Entered passwords do not match", errorText);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//scenario 2
	@When("I confirm the same password {string}")
	public void i_confirm_the_same_password(String string) {
		WebElement confirmpassword = RunCucumberTests.driver.findElement(By.id("confirmpassword"));
		confirmpassword.sendKeys(string);
	}
	
	// Successful register
    @Then("I should be taken to the login page")
    public void i_should_be_taken_to_the_login_page() {
    	try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    	assertEquals("https://localhost:8443/login.jsp", RunCucumberTests.driver.getCurrentUrl());
    }
    
    // Scenario: When registering, password should be hidden
    @Then("my password should be hidden")
    public void my_password_should_be_hidden() {
        WebElement passwordInput = RunCucumberTests.driver.findElement(By.id("password"));
        assertEquals("password", passwordInput.getAttribute("type"));
    }

}
