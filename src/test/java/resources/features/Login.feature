@LoginPage @TestNG
@severity=blocker
Feature: Feature - Login to HRM Application

  @ValidCredentials
  Scenario: Scenario - Login with valid credentials

    Given User is on Home page
    When User enters username as "Admin"
    And User enters password as "admin123"
    Then User should be able to login sucessfully

  @InvalidCredentials
  Scenario Outline: Scenario -Login with invalid credentials

    Given User is on Home page
    When User enters username as "<username>"
    And User enters password as "<password>"
    Then Error message "<message>" should be displayed

    Examples:
      |username  |password  |message                         |
      |admin     |admin     |Invalid credentials             |
      |          |admin123  |Username can be empty           |
      |Admin     |          |Password can be empty           |
      |          |          |Username cannot be empty        |