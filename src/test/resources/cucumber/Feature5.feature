Feature: Works on the Chrome web browser and mobile devices

  Scenario: Using the webapp on a mobile device
    Given I am on a mobile device
    When I am logged in
    Then I will see that certain buttons have smaller fonts