Feature: View and compare the historical performance of a stock

 Scenario: Tracking all stocks in the historical portfolio
   Given I am logged in to my account
   When I add stocks to the historical portfolio
   And I click select all portfolio stocks
   Then all historical stocks will have their trackers toggled on
  
 Scenario: Tracking no stocks in the historical portfolio
   Given I am logged in to my account
   When I click deselect all historical stocks
   Then no historical stocks will have their trackers toggled on

  Scenario: Adding a historical stock to portfolio
   Given I am logged in to my account
   When I add a stock to the historical portfolio
   Then I should see the stock I added in the historical portfolio
   
  Scenario: Removing a historical stock from portfolio
   Given I am logged in to my account
   When I add a stock to the historical portfolio
   And I remove the stock from the historical portfolio
   Then I should not see that stock in the historical portfolio 

  Scenario: Adding an invalid historical stock from portfolio
   Given I am logged in to my account
   When I add an invalid "MEME" stock to the historical portfolio
   Then I should see a "MEME stock data not found" error

  Scenario: There should be a View Stock button in the add historical stock modal
   Given I am logged in to my account
   When I click on add historical stock button
   Then I should see a view stock button
   
  Scenario: There should be a cancel button in the add historical stock modal
   Given I am logged in to my account
   When I click on add historical stock button
   Then I should see a cancel button

