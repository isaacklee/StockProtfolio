Feature: The application must be secure and protect confidentiality of users’ data
   
  Scenario: Trying to access dashboard without being logged in
    Given I am not logged in
    When I change my url to "https://localhost:8443/dashboard.jsp"
    Then I will be redirected to "https://localhost:8443/login.jsp"
  
  Scenario: Logging out safely
    Given I am already logged in
    When I click on the logout button
    Then I will be in "https://localhost:8443/login.jsp"
    
  Scenario: Timeout logs out user
    Given I am already logged in
    And I wait for two minutes
    Then I will be at "https://localhost:8443/logout.jsp"
  
  Scenario: Three failed login attempts
    Given I am at page "https://localhost:8443/login.jsp"
    When I fail to login three times while meeting username constraints, "asdf"
    And enter password constraints, "12341234" 
    And click signin button three times
    Then I should see a error message displaying the time I'm locked out for
    
  Scenario: Logging in after two failed attempts after a minute
    Given I am at page "https://localhost:8443/login.jsp"
    When I enter username constraints, "asdf"
    And enter the password constraints, "12341234" 
    And click signin button two times
    And I wait a minute
    And click signin button one time
    Then I should not be locked out
    
  Scenario: Checking SSL
  	Given I am at page "https://localhost:8443"
  	Then I should have https in the URL
  	
  	
