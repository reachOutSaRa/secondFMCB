Feature: Success Page

  Scenario Outline: To test if order is placed successfully
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
    And Click on the PROCEED TO BILLING option
    Then Assert it is redirected to Order confirmation page
    And Assert that the <"successMessage"> is shown
    And Assert the orderId is created
    And Assert under Order Summary segment selected Delivery Method is listed

  Examples:
    | pinCode  | deliveryMethod | price | successMessage                                      |
    | 15000    | regular        | 42    | Thank you for choosing  to buy from us.             |


