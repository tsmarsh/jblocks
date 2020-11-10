Feature: Submitting a json file for validation
  Scenario: Submitting a good json file
    Given I have I have the file "/schema/good.json"
    When I post it to the service
    Then I should receive a 200
    And the body should be "/schema/good.json"

