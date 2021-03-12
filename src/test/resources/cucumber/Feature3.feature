Feature: Track and visualize changes in value over time of user’s portfolio
 
  Scenario: Tracking all stocks in the portfolio
    Given I am logged in to my account
    When I add stocks to my portfolio 
    And I click select all
    Then all stocks will have their trackers toggled on
  
  Scenario: Tracking no stocks in the portfolio
    Given I am logged in to my account
    When I click deselect all
    Then no stocks will have their trackers toggled on
 
 Scenario: Clicking on the - zoom button will show 1 year of history
   Given I am logged in to my account
   When click the - zoom button
   Then I should see the left side of the graph will be a year before today


 Scenario: Clicking on the + zoom button will show 6 months of history
   Given I am logged in to my account
   When click the + zoom button
   Then I should see the left side of the graph will be 6 months before today

 Scenario: Specifying dates on graph will display dates desired by user
   Given I am logged in to my account
   When I specify dates on graph
   Then I should see the graph endpoints display the dates specified
  @Chosen 
 Scenario: Clicking on the “Days” button will format x-axis of graph to be displayed in days
   Given I am logged in to my account
   When I click on the Days button
   Then I should see the graph of x-axis labels displayed in days
   
Scenario: Clicking on the “Weeks” button will format x-axis of graph to be displayed in weeks
   Given I am logged in to my account
   When I click on the Weeks button
   Then I should see the graph of x-axis labels displayed in weeks
 
 Scenario: Clicking on the “Months” button will format x-axis of graph to be displayed in months
   Given I am logged in to my account
   When I click on the Months button
   Then I should see the graph of x-axis labels displayed in months

 Scenario: S&P 500 is default historical stock
   Given I am logged in to my account
   When I look at my historical stocks
   Then I should see S&P 500 as the first stock in the historical table
   
