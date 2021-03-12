Feature: Must be implemented as a client-server based web application

  
  Scenario: Accessing the webapp server
    Given I am at "https://localhost:8443"
    Then I will see a header which says "USC CS 310 Stock Portfolio Management"