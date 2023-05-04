Feature: Create Space

  Scenario: 'User Creates a Space'
    When 'gus' creates a space with details:
      | name | description             |
      | Home | This is my main address |
    Then 'gus' list spaces and sees:
      | name | description             |
      | Home | This is my main address |
