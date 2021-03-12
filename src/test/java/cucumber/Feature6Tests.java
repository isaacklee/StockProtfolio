package cucumber;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.mysql.cj.jdbc.Driver;

import csci310.servlets.LoginServlet;

import static org.junit.Assert.assertEquals;

public class Feature6Tests {
    private static final String ROOT_URL = "https://localhost:8443/";

    // scenario 1
    @Given("I am not logged in")
    public void i_am_not_logged_in() {
    	RunCucumberTests.driver.get(ROOT_URL);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @When("I change my url to {string}")
    public void i_change_my_url(String string) {
    	RunCucumberTests.driver.get(string);
    }

    @Then("I will be redirected to {string}")
    public void i_should_get_redirected_to_login_page(String string) {
        /*String currentUrl = driver.getCurrentUrl();
        assertEquals(string,currentUrl);*/
        WebElement loginButton = RunCucumberTests.driver.findElement(By.xpath("//*[@id=\"inner\"]/input[3]"));
        loginButton.click();
        WebElement error = RunCucumberTests.driver.findElement(By.id("error"));
        String errorText = error.getText();
        assertEquals("Enter a username with at least 4 characters",errorText);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    // scenario 2
    @Given("I am already logged in")
    public void i_am_on_the_dashboard_page() {
    	RunCucumberTests.driver.get(ROOT_URL + "login.jsp");
        WebElement username = RunCucumberTests.driver.findElement(By.id("username"));
        username.sendKeys("asdf");
        WebElement password = RunCucumberTests.driver.findElement(By.id("password"));
        password.sendKeys("asdfasdf");  
        WebElement loginButton = RunCucumberTests.driver.findElement(By.xpath("//*[@id=\"inner\"]/input[3]"));
        loginButton.click();
    }
    
    @When("I click on the logout button")
    public void i_click_a_logoutButton() {
    	try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        WebElement logoutButton = RunCucumberTests.driver.findElement(By.xpath("//*[@id=\"logout\"]"));
        logoutButton.click();
    }

    @Then("I will be in {string}")
    public void i_should_be_redirected_to_login_page(String string) {
        /*String currentUrl = driver.getCurrentUrl();
        assertEquals(currentUrl,string);*/
    	 try {
             Thread.sleep(2000);
         } catch (InterruptedException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         }
    	WebElement loginButton = RunCucumberTests.driver.findElement(By.xpath("//*[@id=\"inner\"]/input[3]"));
        loginButton.click();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        WebElement error = RunCucumberTests.driver.findElement(By.id("error"));
        String errorText = error.getText();
        assertEquals("Enter a username with at least 4 characters",errorText);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    // Timeout
    @Given("I wait for two minutes")
    public void i_wait_for_two_minutes() {
    	try {
            Thread.sleep(120000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Then("I will be at {string}")
    public void i_will_be_at(String string) {
    	try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertEquals(string, RunCucumberTests.driver.getCurrentUrl());
    }
    
    //Scenario 5
    @Given("I am at page {string}")
    public void i_am_on_the_login_page(String string) {
    	RunCucumberTests.driver.get(string);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    @When("I fail to login three times while meeting username constraints, {string}")
    public void i_enter_into_the_username_box(String string) {
        WebElement username = RunCucumberTests.driver.findElement(By.id("username"));
        username.sendKeys(string);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    @When("enter password constraints, {string}")
    public void i_enter_into_the_password_box(String string) {
        WebElement password = RunCucumberTests.driver.findElement(By.id("password"));
        password.sendKeys(string);  
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    @When("click signin button three times")
    public void i_click_signin_three_times() {
        WebElement loginButton = RunCucumberTests.driver.findElement(By.xpath("//*[@id=\"inner\"]/input[3]"));
        loginButton.click();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        loginButton.click();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        loginButton.click();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Then("I should see a error message displaying the time I'm locked out for")
    public void i_should_see_a_time_error() {
    	try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    	WebElement error = RunCucumberTests.driver.findElement(By.id("error"));
        String errorText = error.getText();
        assertEquals("You have been locked out from logging in for", errorText.substring(0, 44));
    }
    
  //two failed attempts, wait a minute, and another failed attempt should not result in a lockout
    @When("I enter username constraints, {string}")
    public void enter_into_the_username_box(String string) {
        WebElement username = RunCucumberTests.driver.findElement(By.id("username"));
        username.sendKeys(string);
    }
    
    @Then("I should have https in the URL")
    public void i_should_have_https_in_the_URL() {
    	try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	String currURL = RunCucumberTests.driver.getCurrentUrl();
        assertEquals("https://localhost:8443/", currURL);
    }

    @After()
    public void after() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    @When("enter the password constraints, {string}")
    public void enter_into_the_password_box(String string) {
        WebElement password = RunCucumberTests.driver.findElement(By.id("password"));
        password.sendKeys(string);  
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    @When("click signin button two times")
    public void i_click_signin_two_times() {
        WebElement loginButton = RunCucumberTests.driver.findElement(By.xpath("//*[@id=\"inner\"]/input[3]"));
        loginButton.click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        loginButton.click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @When("I wait a minute")
    public void wait_one_minute()
    {
    	try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @When("click signin button one time")
    public void click_signin_once()
    {
    	WebElement loginButton = RunCucumberTests.driver.findElement(By.xpath("//*[@id=\"inner\"]/input[3]"));
        loginButton.click();
    }
    
    @Then("I should not be locked out")
    public void check_if_locked_out()
    {
    	try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    	WebElement error = RunCucumberTests.driver.findElement(By.id("error"));
        String errorText = error.getText();
        assertEquals("The entered username/password does not exist", errorText);
    }
    
}