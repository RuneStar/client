# game-raw

* A set of interfaces representing the game's classes. These provide access to fields, methods, and method
 callbacks. Static members are accessible through the Client interface. Access to fields is provided
 through getters and setters. Callbacks for methods are found as static fields.

* A singleton for accessing the `client` class instance. It is not initialized here but must
 be done elsewhere (This is done in the [client](https://github.com/RuneSuite/client/tree/master/client)
 module).
 
#### Build Dependencies:

* [game-raw-accessors-maven-plugin](https://github.com/RuneSuite/client/tree/master/game-raw-accessors-maven-plugin)

Generates all of the accessor interfaces.