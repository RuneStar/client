# game-raw

Direct access to game values through:
 
* A set of interfaces representing the game's classes. These provide access to fields, methods, and method
 callbacks. Static members are accessible on the Client interface. Access to fields is provided
 through getters and setters. Callbacks for methods are found as static fields.

* A singleton for accessing the `client` class instance.
 
 Generated sources available [here](http://repo.runestar.org/com/RuneStar/client/client-game-raw/).
 
#### Build Dependencies:

* [game-raw-accessors-maven-plugin](https://github.com/RuneStar/client/tree/master/game-raw-accessors-maven-plugin) - Generates all of the accessor interfaces.