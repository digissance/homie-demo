Feature: Create a Space with several rooms and elements in it

  Background:
    Given user with name 'Gus'

  Scenario: 'User Creates a Space and rooms within that space'
    When user creates a space with details:
      | name | description             |
      | Home | This is my main address |
    Then user list spaces and sees:
      | name | description             |
      | Home | This is my main address |
    When user creates following rooms in space 'Home':
      | name              | description                       |
      | Bedroom 1         | This is the master bedroom        |
      | Office            | This is the office and misc stuff |
      | Kitchen           | The one and only kitchen          |
      | Bathroom 1        | Adults bathroom with shower       |
      | Bathroom 2 (Kids) | Kids bathroom with tub            |
      | Living room       | Living room                       |
    Then user list rooms of space 'Home' and sees the same rooms as described above
    When user creates following storage units in different rooms:
      | room              | name               | description                                                       |
      | Bedroom 1         | Wardrobe Left      | Wardrobe next to the window, guy's clothes and hardware tools     |
      | Bedroom 1         | Wardrobe Right     | Wardrobe next to the door, girl's clothes and lots of shoes       |
      | Bedroom 1         | Drawers Bedroom    | Drawers between wardrobes, socks and underware and some documents |
      | Office            | Sofa Bed Office    | This is the office and misc stuff                                 |
      | Kitchen           | Fridge             | The one and only fridge                                           |
      | Kitchen           | Pantry             | Dry food and cookies                                              |
      | Bathroom 1        | Drawers Bathroom 1 | Toiletry stuff                                                    |
      | Bathroom 2 (Kids) | Drawers Bathroom 2 | Toiletry stuff and hair dryer                                     |
      | Bathroom 2 (Kids) | Shelves            | Towels                                                            |
      | Living room       | Wardrobe LR        | Baby stuff and medicines                                          |
      | Living room       | Sofa Bed LR        | Blankets and pillows                                              |
      | Living room       | Tv Closet          | Books and documents                                               |
      | Shelves           | Top Shelf          | Towels                                                            |
      | Shelves           | Middle Shelf       | Towels                                                            |
      | Shelves           | Bottom Shelf       | Towels                                                            |
    Then all the storage units listed above exist in the space 'Home'
    When user creates following items in different storage units:
      | storage            | name           | description |
      | Wardrobe Left      | Hammer         |             |
      | Wardrobe Right     | Shoes          |             |
      | Drawers Bedroom    | Socks          |             |
      | Sofa Bed Office    | Pillows        |             |
      | Fridge             | Strawberry Jam |             |
      | Pantry             | Cookies        |             |
      | Drawers Bathroom 1 | Toothbrush     |             |
      | Drawers Bathroom 2 | Soap           |             |
      | Top Shelf          | Gray Towel     |             |
      | Middle Shelf       | Green Towel    |             |
      | Bottom Shelf       | Bath Toys      |             |
      | Wardrobe LR        | Aspirin        |             |
      | Sofa Bed LR        | Blankets       |             |
      | Tv Closet          | Tv Guide       |             |
      | Living room        | Tv Remote      |             |
    Then all the items listed above exist in the space 'Home'

