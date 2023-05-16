Feature: Check permissions when deleting rooms storage and items

  Background:
    Given user with name 'Gus'
    And user logs in
    And space home with rooms and items are created

  Scenario: 'Unauthorized User creates elements and is denied'
    When user with name 'Pepe'
    And user logs in
    And user creates following rooms in space 'Home' and permission is denied:
      | name                  | description                |
      | Bedroom No Permission | This is the master bedroom |
    And user creates following storage in room 'Kitchen' and permission is denied:
      | name     | description          |
      | Cupboard | where the plates are |
    And user creates following item in 'Fridge' and permission is denied:
      | name    | description       |
      | Blender | to make smoothies |

  Scenario: 'Unauthorized User deletes elements and is denied'
    When user with name 'Pepe'
    And user logs in
    When user deletes room 'Kitchen' and permission is denied
    And user deletes storage 'Bottom Shelf' and permission is denied
    And user deletes item 'Shoes' and permission is denied

