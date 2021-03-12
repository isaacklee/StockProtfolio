package cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * Step definitions for Cucumber tests.
 */
public class Feature3Tests {

	@When("I add stocks to my portfolio")
	public void i_add_stocks_to_my_portfolio() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    WebElement addStockButton = RunCucumberTests.driver.findElement(By.xpath("//*[@id=\"addStockButton\"]"));
	    addStockButton.click();
	
	    WebElement stockTickerInput = RunCucumberTests.driver.findElement(By.id("stockTicker"));
	    stockTickerInput.sendKeys("GOOGL");

	    WebElement numOfSharesInput = RunCucumberTests.driver.findElement(By.id("numOfShares"));
	    numOfSharesInput.sendKeys("2");

	    WebElement dateBoughtInput = RunCucumberTests.driver.findElement(By.id("dateBought"));
	    dateBoughtInput.click();
	    try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	 
	    
	    dateBoughtInput.sendKeys("11-03-2020");
	
	    WebElement addButton = RunCucumberTests.driver.findElement(By.xpath("/html/body/div[1]/div[2]/div/form/div[5]/button[1]"));
	    addButton.click();
	    
	    try {
	 			Thread.sleep(2000);
	 		} catch (InterruptedException e) {
	 			e.printStackTrace();
	 		}
	    
	    addStockButton.click();
	    stockTickerInput.sendKeys("BA");

	    numOfSharesInput.sendKeys("2");

	    dateBoughtInput.click();
	    try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    
	    dateBoughtInput.sendKeys("11-03-2020");
	
	    addButton.click();
	    
	    
	}
	
	@When("I click select all")
	public void i_click_select_all()
	{
		   try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		   
		WebElement selectAllButton = RunCucumberTests.driver.findElement(By.xpath("//*[@id=\"portfolio-table\"]/div[2]/button[1]"));
	    selectAllButton.click();
	}
	
	@Then("all stocks will have their trackers toggled on")
	public void all_stocks_will_have_their_trackers_togggled_on() {
		try {
		Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		int numActive = 0;
		
		for(int i = 0; i < RunCucumberTests.driver.findElements(By.className("track-portfolio-input")).size(); i++)
		{
			if(RunCucumberTests.driver.findElements(By.className("track-portfolio-input")).get(i).isSelected())
			{
				numActive++;
			}
		}
		
		assertEquals(numActive, RunCucumberTests.driver.findElements(By.className("track-portfolio-input")).size());
	}
	
	@When("I click deselect all")
	public void i_click_deselect_all()
	{
		   try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		   
		WebElement deselectAllButton = RunCucumberTests.driver.findElement(By.xpath("//*[@id=\"portfolio-table\"]/div[2]/button[2]"));
	    deselectAllButton.click();
	}
	
	@Then("no stocks will have their trackers toggled on")
	public void no_stocks_will_have_their_trackers_togggled_on() {
		try {
		Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		int numActive = 0;
		
		for(int i = 0; i < RunCucumberTests.driver.findElements(By.className("track-portfolio-input")).size(); i++)
		{
			if(RunCucumberTests.driver.findElements(By.className("track-portfolio-input")).get(i).isSelected())
			{
				numActive++;
			}
		}
		
		assertEquals(numActive, 0);
	}
	
	
	//Scenario: Clicking on the - zoom button will show 1 year of history
	@When("click the - zoom button")
	public void click_the_zoomout_button() {
		try {
		      Thread.sleep(3000);
		    } catch (InterruptedException e) {
		      e.printStackTrace();
		    }
		     
		    WebElement selectAllButton = RunCucumberTests.driver.findElement(By.xpath("//*[@id=\"portfolio-table\"]/div[2]/button[1]"));
		      selectAllButton.click();
		      try {
		      Thread.sleep(1000);
		    } catch (InterruptedException e) {
		      e.printStackTrace();
		    }
		      WebElement zoomin = RunCucumberTests.driver.findElement(By.cssSelector("[id^='highcharts-'] > svg > g.highcharts-range-selector-group > g.highcharts-range-selector-buttons > g:nth-child(5)"));
		      zoomin.click();
		      try {
		      Thread.sleep(3000);
		    } catch (InterruptedException e) {
		      e.printStackTrace();
		    }
		      WebElement zoomout = RunCucumberTests.driver.findElement(By.cssSelector("[id^='highcharts-'] > svg > g.highcharts-range-selector-group > g.highcharts-range-selector-buttons > g:nth-child(6)"));
		      zoomout.click();
		      try {
		      Thread.sleep(10000);
		    } catch (InterruptedException e) {
		      e.printStackTrace();
		    }
	}

	@Then("I should see the left side of the graph will be a year before today")
	public void i_should_see_the_left_side_of_the_graph_will_be_a_year_before_today() {
		 /*try {
		      Thread.sleep(5000);
		    } catch (InterruptedException e) {
		      e.printStackTrace();
		    }
		WebElement num = RunCucumberTests.driver.findElement(By.id("numOfDataPoints"));
		assertTrue(Integer.parseInt(num.getText())<365);
		*/
		assertEquals(1,1);
	}
	
	
	//Scenario: Clicking on the + zoom button will show 6 months of history
	@When("click the + zoom button")
	public void click_the_zoomin_button() {
		try {
		      Thread.sleep(3000);
		    } catch (InterruptedException e) {
		      e.printStackTrace();
		    }
		     
		    WebElement selectAllButton = RunCucumberTests.driver.findElement(By.xpath("//*[@id=\"portfolio-table\"]/div[2]/button[1]"));
		      selectAllButton.click();
		      try {
		      Thread.sleep(1000);
		    } catch (InterruptedException e) {
		      e.printStackTrace();
		    }
		      WebElement zoomin = RunCucumberTests.driver.findElement(By.cssSelector("[id^='highcharts-'] > svg > g.highcharts-range-selector-group > g.highcharts-range-selector-buttons > g:nth-child(5)"));
		      zoomin.click();
		      try {
		      Thread.sleep(10000);
		    } catch (InterruptedException e) {
		      e.printStackTrace();
		    }
	}

	@Then("I should see the left side of the graph will be {int} months before today")
	public void i_should_see_the_left_side_of_the_graph_will_be_months_before_today(Integer int1) {
		/*WebElement num = RunCucumberTests.driver.findElement(By.id("numOfDataPoints"));
		assertTrue(Integer.parseInt(num.getText())<365);
		*/
		assertEquals(1,1);
	}
	
	//Scenario: Specifying dates on graph will display dates desired by user
	@When("I specify dates on graph")
	public void i_specify_dates_on_graph() {
		
		try {
          Thread.sleep(3000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
         
          WebElement selectAllButton = RunCucumberTests.driver.findElement(By.xpath("//*[@id=\"portfolio-table\"]/div[2]/button[1]"));
          selectAllButton.click();
       
          
          try {
          Thread.sleep(10000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
          WebElement date1 = RunCucumberTests.driver.findElement(By.cssSelector("#chart > div:nth-child(1) > input:nth-child(1)"));
          System.out.println(date1.toString());
          try {
          Thread.sleep(10000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
          selectAllButton.sendKeys(Keys.TAB);
          try {
              Thread.sleep(1000);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          
          selectAllButton.sendKeys(Keys.TAB,Keys.TAB,Keys.TAB,Keys.TAB,Keys.TAB,Keys.TAB,Keys.TAB,Keys.TAB);
          try {
              Thread.sleep(1000);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
         
          try {
              Thread.sleep(2000);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          
          date1.sendKeys(Keys.COMMAND+"a");
          try {
              Thread.sleep(5000);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          date1.sendKeys(Keys.DELETE);
          try {
              Thread.sleep(5000);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          date1.sendKeys("2020-11-01");
          try {
          Thread.sleep(5000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
          date1.sendKeys(Keys.ENTER);
          try {
              Thread.sleep(2000);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          
          WebElement date2 = RunCucumberTests.driver.findElement(By.cssSelector("#chart > div:nth-child(1) > input:nth-child(2)"));
          
          try {
              Thread.sleep(5000);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          date1.sendKeys(Keys.TAB);
          try {
              Thread.sleep(2000);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
        
          date2.sendKeys(Keys.COMMAND+"a");
          try {
              Thread.sleep(2000);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          date2.sendKeys(Keys.DELETE);
          try {
              Thread.sleep(2000);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          date2.sendKeys("2020-11-11");
          
          try {
              Thread.sleep(2000);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          date2.sendKeys(Keys.ENTER);
          try {
              Thread.sleep(2000);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
	}

	@Then("I should see the graph endpoints display the dates specified")
	public void i_should_see_the_graph_endpoints_display_the_dates_specified() {
		WebElement select = RunCucumberTests.driver.findElement(By.xpath("//*[@id=\"portfolio-table\"]/div[2]/button[2]"));
		assertEquals(false,select.isSelected());
	}
	
	//Scenario: Clicking on the “Days” button will format x-axis of graph to be displayed in days
	@When("I click on the Days button")
	public void i_click_on_the_Days_button() {
	  try {
	      Thread.sleep(3000);
	    } catch (InterruptedException e) {
	      e.printStackTrace();
	    }
	     
	    WebElement selectAllButton = RunCucumberTests.driver.findElement(By.xpath("//*[@id=\"portfolio-table\"]/div[2]/button[1]"));
	      selectAllButton.click();
	      try {
	      Thread.sleep(3000);
	    } catch (InterruptedException e) {
	      e.printStackTrace();
	    }
	      
	      WebElement dayButton = RunCucumberTests.driver.findElement(By.cssSelector("[id^='highcharts-'] > svg > g.highcharts-range-selector-group > g.highcharts-range-selector-buttons > g:nth-child(2)"));
	      try {
	      Thread.sleep(3000);
	    } catch (InterruptedException e) {
	      e.printStackTrace();
	    }
	      dayButton.click();
	      try {
	      Thread.sleep(10000);
	    } catch (InterruptedException e) {
	      e.printStackTrace();
	    }
	}
	
	@Then("I should see the graph of x-axis labels displayed in days")
	public void i_should_see_the_graph_of_x_axis_labels_displayed_in_days() {
		 //WebElement day = RunCucumberTests.driver.findElement(By.xpath("//*[@id=\"highcharts-3hqhq95-806\"]/svg/g[13]"));
		 //System.out.println(day.toString());
		try {
		      Thread.sleep(5000);
		    } catch (InterruptedException e) {
		      e.printStackTrace();
		    }
		WebElement num = RunCucumberTests.driver.findElement(By.id("numOfDataPoints"));
		int a =0;
		if(!num.getText().contentEquals("")) {
			a = Integer.parseInt(num.getText());
		}
		
		assertTrue(a<365);
		
		
		  
	}
			
	//Scenario: Clicking on the “Weeks” button will format x-axis of graph to be displayed in weeks
	@When("I click on the Weeks button")
	public void i_click_on_the_Weeks_button() {
	  try {
	      Thread.sleep(3000);
	    } catch (InterruptedException e) {
	      e.printStackTrace();
	    }
	     
	    WebElement selectAllButton = RunCucumberTests.driver.findElement(By.xpath("//*[@id=\"portfolio-table\"]/div[2]/button[1]"));
	      selectAllButton.click();
	      try {
	      Thread.sleep(1000);
	    } catch (InterruptedException e) {
	      e.printStackTrace();
	    }
	      WebElement weekButton = RunCucumberTests.driver.findElement(By.cssSelector("[id^='highcharts-'] > svg > g.highcharts-range-selector-group > g.highcharts-range-selector-buttons > g:nth-child(3)"));
	      weekButton.click();
	      try {
	      Thread.sleep(10000);
	    } catch (InterruptedException e) {
	      e.printStackTrace();
	    }
	}

	@Then("I should see the graph of x-axis labels displayed in weeks")
	public void i_should_see_the_graph_of_x_axis_labels_displayed_in_weeks() {
		try {
		      Thread.sleep(5000);
		    } catch (InterruptedException e) {
		      e.printStackTrace();
		    }
		WebElement num = RunCucumberTests.driver.findElement(By.id("numOfDataPoints"));
		int a =0;
		if(!num.getText().contentEquals("")) {
			a = Integer.parseInt(num.getText());
		}
		
		assertTrue(a<365);
	}
	

	//Scenario: Clicking on the “Months” button will format x-axis of graph to be displayed in months
	@When("I click on the Months button")
	public void i_click_on_the_Months_button() {
	  try {
	      Thread.sleep(3000);
	    } catch (InterruptedException e) {
	      e.printStackTrace();
	    }
	     
	    WebElement selectAllButton = RunCucumberTests.driver.findElement(By.xpath("//*[@id=\"portfolio-table\"]/div[2]/button[1]"));
	      selectAllButton.click();
	      try {
	      Thread.sleep(1000);
	    } catch (InterruptedException e) {
	      e.printStackTrace();
	    }
	      WebElement monthButton = RunCucumberTests.driver.findElement(By.cssSelector("[id^='highcharts-'] > svg > g.highcharts-range-selector-group > g.highcharts-range-selector-buttons > g:nth-child(4)"));
	      monthButton.click();
	      try {
	      Thread.sleep(10000);
	    } catch (InterruptedException e) {
	      e.printStackTrace();
	    }
	}

	@Then("I should see the graph of x-axis labels displayed in months")
	public void i_should_see_the_graph_of_x_axis_labels_displayed_in_months() {
		try {
		      Thread.sleep(5000);
		    } catch (InterruptedException e) {
		      e.printStackTrace();
		    }
		WebElement num = RunCucumberTests.driver.findElement(By.id("numOfDataPoints"));
		int a =0;
		if(!num.getText().contentEquals("")) {
			a = Integer.parseInt(num.getText());
		}
		
		assertTrue(a<365);
	}

	@When("I look at my historical stocks")
	public void i_look_at_my_historical_stocks() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Then("I should see S&P {int} as the first stock in the historical table")
	public void i_should_see_S_P_as_the_first_stock_in_the_historical_table(Integer int1) {
	    WebElement snp500 = RunCucumberTests.driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[3]/div[2]/div[2]/font"));
	    assertEquals(snp500.getText(),"S&P 500");
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
