# Directory Structure

This covers the files present in this project.

Some files/subdirectories are skipped because they are self-explanatory or rarely used.

* `.settings`: formatting settings for the project
* `.vscode`: run configurations and other VS Code settings
* `docs`: documentation
* `gradle`: the necessary files to install gradle
* `src`: the Java files
    * `prod`: the production code
        * `main`: the main classes of the program
            * [`Developer.java`](../src/prod/prod/Developer.java): alternative main class with regression test factory
            * [`Main.java`](../src/prod/prod/Main.java): I/O logic, solver logic
        * `brutesolver`: brute-force logic for solving games
            * [`Game.java`](../src/prod/brutesolver/Game.java): interface for any Game
            * [`Solver.java`](../src/prod/brutesolver/Solver.java): logic to solve abstract games
            * [`State.java`](../src/prod/brutesolver/State.java): wrapper for Games that allows implicit linked lists of Games and transition messages
        * `calculatorgame`: a class for each rule in Calculator: The Game
            * [`CalculatorGame.java`](../src/prod/calculatorgame/CalculatorGame.java): the data structure for a game, including the logic to apply portals
            * [`Config.java`](../src/prod/calculatorgame/Config.java): configuration settings, prompts, all that jazz. This should be split into multiple files...
            * [`Helpers.java`](../src/prod/calculatorgame/Helpers.java): miscellaneous helper methods
    * `test`: all test code
        * `main`
            * [`MainTests.java`](../src/test/main/BaseTests.java): comprehensive tests, from unit to end-to-end, covering nearly all functionality of the program
        * `calculatorgame`
            * [`LevelTests.java`](../src/test/calculatorgame/LevelTests.java): tests for the ability to correctly solve levels
* `test-cases`: stores all test case files for the level-solver. File format found in the [testing document](./testing.md#test-cases)
* [`.classpath`](../.classpath): the classpath
* [`.project`](../.project): Metadata about the project
* `*.gradle`: Gradle files for running the project
* [`gradlew`](../gradlew): build script for Unix-y machines
* [`gradlew.bat`](../gradlew.bat): build script for Windows machines
