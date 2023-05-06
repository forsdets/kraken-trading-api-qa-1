@publicapi_book_order
Feature:Validate the public API Book

  @post_book_order
  Scenario: Validate the get book api
    Given the book api service and the headers are available
    And the request is ready with the headers
    And the endpoint path pair as XBTUSD
    And the endpoint path count as 1
    And API post request contains the correct schema structure
    When the post order book details are retrieved from the service
    Then the expected response fields are available to validate
    And the HTTP code should be 200
    And API post response contains the correct schema structure
    And the API actual response should match with the expected response

  @put_book_order
  Scenario: Validate the get book api
    Given the book api service and the headers are available
    And the request is ready with the headers
    And the endpoint path pair as XBTUSD
    And the endpoint path count as 1
    And API put request contains the correct schema structure
    When the get order book details are retrieved from the service
    Then the expected response fields are available to validate
    And the HTTP code should be 200
    And API put response contains the correct schema structure
    And the API actual response should match with the expected response

  @patch_book_order
  Scenario: Validate the get book api
    Given the book api service and the headers are available
    And the request is ready with the headers
    And the endpoint path pair as XBTUSD
    And the endpoint path count as 1
    And API patch request contains the correct schema structure
    When the get order book details are retrieved from the service
    Then the expected response fields are available to validate
    And the HTTP code should be 200
    And API patch response contains the correct schema structure
    And the API actual response should match with the expected response

  @get_book_order
  Scenario: Validate the get book api
    Given the book api service and the headers are available
    And the request is ready with the headers
    And the endpoint path pair as XBTUSD
    And the endpoint path count as 1
    When the get order book details are retrieved from the service
    Then the expected response fields are available to validate
    And the HTTP code should be 200
    And API get response contains the correct schema structure
    And the API actual response should match with the expected response