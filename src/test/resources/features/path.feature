Feature: Get the full path of an element

  Background:
    Given user with name 'Gus'
    And user logs in
    And space home with rooms and items are created

  Scenario: 'Full path of any element'
    When user requests the full path of the element cookies
    Then the full path of the element is returned
      | home    |
      | kitchen |
      | pantry  |
      | cookies |


