Feature: Shopping Cart

  Scenario: To test the checking out of items in cart
    Given Landing on the Shopping page
    Then Assert the logo of the retailer
    And Assert the items are listed in the shopping cart

  Scenario: To test the items and its details are non editable
    Given Landing on the Shopping page
    Then Assert the logo of the retailer
    When Clicking on the close icon in the item segment
    Then Assert that the items cannot be removed
    And Click on quantity in the item segment
    And Assert that the quantity cannot be changed

  Scenario: To test the price of items in cart and in order summary
    Given Landing on the Shopping page
    Then Assert the logo of the retailer
    When Items are listed in the shopping cart
    Then Assert the items listed has its corresponding price
    And Under order summary assert your cart has total price of items in cart
    And Assert the delivery mode and its price
    And Assert the order total

  Scenario: To Test the checking out of items in cart
    Given Landing on the Shopping page
    Then Assert the logo of the retailer
    When Items are listed in the shopping cart
    Then Click on CheckOut option
    And Assert it is redirected to the delivery selection page
    And Assert the logo of the retailer

