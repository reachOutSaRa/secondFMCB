Feature: Delivery Selection Page

  #Check the delivery method price for boundary values
  Scenario Outline: To test RatePerKG and Rate of regular and express method getting updated with the change in PinCode
    Given Landing on the Shopping page
    Then Assert the logo of the retailer
    When Items are listed in the shopping cart
    Then Click on CheckOut option
    And Assert it is redirected to the delivery selection page
    And Assert the logo of the retailer
    Then Enter the delivery information
    And Enter the delivery address with <"pinCode">
    Then Assert the <"ratePerKG"> <"serviceOwner"> and rate for <"regular"> and <"express"> method of delivery options

    Examples:
    | pinCode  | ratePerKG | serviceOwner             | regular | express |
    | 15000    | 0.5       | American Airlines Cargo  | 42      | 42      |
    | 15236    | 0.5       | American Airlines Cargo  | 42      | 42      |
    | 10115    | 1.0       | American Airlines Cargo  | 84      | 84      |
    | 14199    | 1.0       | American Airlines Cargo  | 84      | 84      |
    | BR1      | 0.5       | American Airlines Cargo  | 42      |         |
    | BR6      | 0.5       | American Airlines Cargo  | 42      |         |
    | EC1A     | 0.5       | American Airlines Cargo  | 42      | 84      |
    | BR1      | 1.0       | American Airlines Cargo  |         | 84      |
    | BR6      | 1.0       | American Airlines Cargo  |         | 84      |
    | EC1A     | 1.0       | American Airlines Cargo  |         | 84      |
    | 14999    | 0.5       | American Airlines Cargo  | 42      | 42      |
    | 15237    | 0.5       | American Airlines Cargo  | 42      | 42      |
    | 10114    | 1.0       | American Airlines Cargo  | 84      | 84      |
    | 14198    | 1.0       | American Airlines Cargo  | 84      | 84      |
    | XYZ      | 0.5       | American Airlines Cargo  | 42      |         |
    | XYZA     | 1.0       | American Airlines Cargo  |         | 84      |


  Scenario Outline: To test the delivery method and price updated in order summary with the selection of delivery method
    Given Landing on the Shopping page
    Then Assert the logo of the retailer
    When Items are listed in the shopping cart
    Then Click on CheckOut option
    And Assert it is redirected to the delivery selection page
    And Assert the logo of the retailer
    Then Enter the delivery information
    And Enter the delivery address with <"pinCode">
    Then Select on the <"deliveryMethod">
    And Assert the <"price"> of the delivery method
    Then Assert Under order summary <"deliveryMethod"> and its <"price"> are updated correctly
    And Assert the total of the order summary

    Examples:
    | pinCode  | deliveryMethod | price |
    | 15000    | regular        | 42    |

