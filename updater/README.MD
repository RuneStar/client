# updater
 
* hooks: the mapping between the obfuscated bytecode element names and
 their actual names. Includes other data such as field decoder multipliers and 
 method opaque predicate argument values.

* the original gamepack jar (~2 MB)

* a deobfuscated gamepack jar (~500 KB)

[Download](http://repo.runestar.org/org/runestar/client/client-updater/)

#### Dependencies:

* [updater-common](https://github.com/RuneStar/client/tree/master/updater-common)


#### Build dependencies:

* [general](https://github.com/RuneStar/general) - Utilities for getting the current game revision and downloading the current gamepack jar

* [updater-deob](https://github.com/RuneStar/client/tree/master/updater-deob) - Does various bytecode deobfuscation, transformation, and analysis

* [updater-mapper](https://github.com/RuneStar/client/tree/master/updater-mapper) - Framework for mapping obfuscated names to their actual names

* [updater-mapper-standard](https://github.com/RuneStar/client/tree/master/updater-mapper-standard) - Implementation of updater-mapper for Old School RuneScape

* [updater-create-maven-plugin](https://github.com/RuneStar/client/tree/master/updater-create-maven-plugin) - Downloads the current gamepack jar and generates the needed resources

Testing done in [updater-testing](https://github.com/RuneStar/client/tree/master/updater-testing)