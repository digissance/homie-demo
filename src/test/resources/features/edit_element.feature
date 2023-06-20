Feature: Edit elements names and descriptions

  Background:
    Given user with name 'Gus'
    And user logs in

  Scenario: 'Edit space name'
    And user creates following elements:
      | type    | name        | description             | parent   |
      | space   | home        | where I live            |          |
      | room    | bedroom     | where I sleep           | home     |
      | room    | office      | where I work            | home     |
      | room    | kitchen     | where I cook            | home     |
      | room    | bathroom    | where I shower          | home     |
      | storage | wardrobe    | where my clothes are    | bedroom  |
      | storage | desk        | where my documents are  | office   |
      | storage | shelves     | where my toiletries are | bathroom |
      | storage | pantry      | where my food is        | bathroom |
      | storage | shirts      | my shirts               | wardrobe |
      | storage | passport    | my passport             | desk     |
      | storage | tooth brush | my tooth brush          | shelves  |
      | storage | cookies     | my cookies              | pantry   |
    When user edits name of element home to 'my home' is a success

  Scenario: 'Edit room name'
    And user creates following elements:
      | type    | name        | description             | parent   |
      | space   | home        | where I live            |          |
      | room    | bedroom     | where I sleep           | home     |
      | room    | office      | where I work            | home     |
      | room    | kitchen     | where I cook            | home     |
      | room    | bathroom    | where I shower          | home     |
      | storage | wardrobe    | where my clothes are    | bedroom  |
      | storage | desk        | where my documents are  | office   |
      | storage | shelves     | where my toiletries are | bathroom |
      | storage | pantry      | where my food is        | bathroom |
      | storage | shirts      | my shirts               | wardrobe |
      | storage | passport    | my passport             | desk     |
      | storage | tooth brush | my tooth brush          | shelves  |
      | storage | cookies     | my cookies              | pantry   |
    When user edits name of element bedroom to 'master home' is a success

  Scenario: 'Edit storage name'
    And user creates following elements:
      | type    | name        | description             | parent   |
      | space   | home        | where I live            |          |
      | room    | bedroom     | where I sleep           | home     |
      | room    | office      | where I work            | home     |
      | room    | kitchen     | where I cook            | home     |
      | room    | bathroom    | where I shower          | home     |
      | storage | wardrobe    | where my clothes are    | bedroom  |
      | storage | desk        | where my documents are  | office   |
      | storage | shelves     | where my toiletries are | bathroom |
      | storage | pantry      | where my food is        | bathroom |
      | storage | shirts      | my shirts               | wardrobe |
      | storage | passport    | my passport             | desk     |
      | storage | tooth brush | my tooth brush          | shelves  |
      | storage | cookies     | my cookies              | pantry   |
    When user edits name of element desk to 'office desk' is a success


  Scenario: 'Edit item name'
    And user creates following elements:
      | type    | name        | description             | parent   |
      | space   | home        | where I live            |          |
      | room    | bedroom     | where I sleep           | home     |
      | room    | office      | where I work            | home     |
      | room    | kitchen     | where I cook            | home     |
      | room    | bathroom    | where I shower          | home     |
      | storage | wardrobe    | where my clothes are    | bedroom  |
      | storage | desk        | where my documents are  | office   |
      | storage | shelves     | where my toiletries are | bathroom |
      | storage | pantry      | where my food is        | bathroom |
      | storage | shirts      | my shirts               | wardrobe |
      | storage | passport    | my passport             | desk     |
      | storage | tooth brush | my tooth brush          | shelves  |
      | storage | cookies     | my cookies              | pantry   |
    When user edits name of element passport to 'id document' is a success