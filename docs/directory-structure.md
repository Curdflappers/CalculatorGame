# Directory Structure

This covers the files present in this project.

Some files/subdirectories are skipped because they are self-explanatory or rarely used.

* `.settings`: formatting settings for the project
* `.vscode`: run configurations and other VS Code settings
* `docs`: documentation
* `gradle`: the necessary files to install gradle
* `src`: the Java files
    * `main`: the production code
        * `base`: the basics of the program
            * [`Config.java`](../src/main/base/Config.java): configuration settings, prompts, all that boring jazz
            * [`CalculatorGame.java`](../src/main/base/CalculatorGame.java): the data structure for a game, including the logic to apply portals
            * [`Helpers.java`](../src/main/base/Helpers.java): miscellaneous helper methods
            * [`Main.java`](../src/main/base/Main.java): I/O logic, solver logic
            * [`State.java`](../src/main/base/State.java): wrapper for Games that allows implicit linked lists of Games with some extra functionality
        * `rules`: a class for each rule in the game
    * `test`: test code
        * `base`: tests for the base package
            * [`BaseTests.java`](../src/test/base/BaseTests.java): many many tests, from unit to end-to-end, covering the general functionality of the program
            * [`GameTests.java`](../src/test/base/GameTests.java): tests relating to the CalculatorGame data structure, mainly portal validation and behavior
            * [`HelperTests.java`](../src/test/base/HelperTests.java): unit tests for the methods in `Helpers.java`
            * [`LevelTests.java`](../src/test/base/HelperTests.java): tests for the ability to correctly solve levels
            * [`MainTests.java`](../src/test/base/MainTests.java): tests methods within `Main.java`
        * `rules`: tests for the rules package
* `test-cases`: stores all test case files for the level-solver. File format found in the [testing document](./testing.md#test-cases)
* [`.classpath`](../.classpath): the classpath
* [`.gitignore`](../.gitignore): self-explanatory
* [`.project`](../.project): Metadata about the project
* `*.gradle`: Gradle files for running the project
* [`gradlew`](../gradlew): build script for Unix-y machines
* [`gradlew.bat`](../gradlew.bat): build script for Windows machines
* [`README.md`](../README.md): the README
