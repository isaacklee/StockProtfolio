Feature: Ability to add or remove stocks from the portfolio

  Scenario: Adding a stock to the portfolio without a sell date (successful)
    Given I am logged in to my account
    When I click on the "addStockButton" button
    And I enter "AAPL" into the Stock Ticker input
    And I enter "69" into the number of shares input
    And I enter "11-03-2020" into the Date Bought input
    And I click the Add Stock button
    Then the "ticker-input-error" font element will be empty
 
  Scenario: Removing a stock from the portfolio
    Given I am logged in to my account
    When I click on a remove button for a portfolio stock
    And I click on the Delete Stock button
    Then that stock will not appear in the portfolio
  
  Scenario: Adding a stock to the portfolio with a sell date (successful)
    Given I am logged in to my account
    When I click on the "addStockButton" button
    And I enter "PLTR" into the Stock Ticker input
    And I enter "69" into the number of shares input
    And I enter "11-03-2020" into the Date Bought input
    And I enter "11-04-2020" into the Date Sold input
    And I click the Add Stock button
    Then the "ticker-input-error" font element will be empty
  
  Scenario: Adding a stock to the portfolio with a sell date before the purchase date (unsuccessful)
    Given I am logged in to my account
    When I click on the "addStockButton" button
    And I enter "AAPL" into the Stock Ticker input
    And I enter "69" into the number of shares input
    And I enter "11-04-2020" into the Date Bought input
    And I enter "11-03-2020" into the Date Sold input
    And I click the Add Stock button
    Then the "dateSold-input-error" font element will display "Sell date must be after purchase date"
    
  Scenario: Adding a stock to the portfolio with no purchase date (unsuccessful)
    Given I am logged in to my account
    When I click on the "addStockButton" button
    And I enter "SNOW" into the Stock Ticker input
    And I enter "69" into the number of shares input
    And I click the Add Stock button
    Then the "dateBought-input-error" font element will display "Please use a valid date"
    
  Scenario: Adding a stock to the portfolio with a ticker that doesn't exist (unsuccessful)
    Given I am logged in to my account
    When I click on the "addStockButton" button
    And I enter "YEET" into the Stock Ticker input
    And I enter "69" into the number of shares input
    And I enter "11-03-2020" into the Date Bought input
    And I click the Add Stock button
    Then the "ticker-input-error" font element will display "Not a valid stock ticker"
  
  Scenario: Adding a stock to the portfolio with an negative number of shares (unsuccessful)
    Given I am logged in to my account
    When I click on the "addStockButton" button
    And I enter "AAPL" into the Stock Ticker input
    And I enter "-69" into the number of shares input
    And I enter "11-03-2020" into the Date Bought input
    And I click the Add Stock button
    Then the "numShares-input-error" font element will display "The number of shares must be a positive integer"
    
  Scenario: Adding a stock to the portfolio with an 0 number of shares (unsuccessful)
    Given I am logged in to my account
    When I click on the "addStockButton" button
    And I enter "AAPL" into the Stock Ticker input
    And I enter "0" into the number of shares input
    And I enter "11-03-2020" into the Date Bought input
    And I click the Add Stock button
    Then the "numShares-input-error" font element will display "The number of shares must be a positive integer"
