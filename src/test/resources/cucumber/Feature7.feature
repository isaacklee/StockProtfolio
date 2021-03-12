Feature: Users must be able to create an account in the system
  
  Scenario: Open dashboard page after successfully logging in
    Given I am on the login page
    When I enter valid "asdf" into the username box
    And I enter corresponding "asdfasdf" into the password box
    And I click the login button
    Then I should be taken to the dashboard page
  
  Scenario: Incorrect password error
    Given I am on the login page
    When I enter valid "TommyTrojan" into the username box
    And I enter the wrong password "iloveUSC" into the password box
    And I click the login button
    Then I should get an entered username or password does not exist error
  
  Scenario: Successful register
    Given I am on the signup page
    When I enter "USCamazing" into the username box
    And I enter a password "UCLAworst" 
    And I confirm the same password "UCLAworst" 
    And I click the signup button
    Then I should be taken to the login page
      
  Scenario: Attempting to register a taken username
    Given I am on the signup page
    When I enter "USCamazing" into the username box
    And I enter a password "UCLAworst" 
    And I confirm the same password "UCLAworst" 
    And I click the signup button
    Then I should get a username is taken error
  
  Scenario: Passwords do not match
    Given I am on the signup page
    When I enter "TommyTrojan" into the username box
    And I enter a password "hellohello" 
    And I confirm a different password "heyhellohey" 
    And I click the signup button
    Then I should get a passwords do not match error
    
  Scenario: When registering, password should be hidden
    Given I am on the signup page
    When I enter "TommyTrojan" into the username box
    And I enter a password "hellohello" 
    And I confirm a different password "heyhellohey" 
    Then my password should be hidden
    
    
    
    
