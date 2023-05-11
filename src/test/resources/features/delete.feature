Feature: Delete rooms storage and items

  Background:
    Given user with name 'Gus'
    And user logs in
    And space home with rooms and items are created

  Scenario: 'User deletes a room'
    When user deletes room 'Kitchen'
    And user deletes storage 'Bottom Shelf'
    And user deletes item 'Shoes'

