Feature: Users can broadcast messages to anyone subscribed to the global chatroom

  Background:
    Given the components "engine,receiver,sender"

  Scenario: A single message is sent to the chatroom
    When "bob" sends a message containing text "Hello room"
    Then the chatroom contains the messages
      | user | text       |
      | bob  | Hello room |

  Scenario: Multiple messages are sent to the chatroom
    When "bob" sends a message containing text "Hello room"
    And "alice" sends a message containing text "Alright?"
    And "frances" sends a message containing text "So-so"
    Then the chatroom contains the messages
      | user    | text       |
      | bob     | Hello room |
      | alice   | Alright?   |
      | frances | So-so      |
