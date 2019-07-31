# Directory Structure

This covers the files present in this project.

Some files/subdirectories are skipped because they are self-explanatory or rarely used.

* `.settings`: formatting settings for the project
* `.vscode`: run configurations and other Visual Studio Code settings
* `docs`: documentation
* `src`: Java files
  * `main/java/com/mathwithmark/calculatorgamesolver`: production code
    * `brutesolver`: brute-force logic for solving games
      * [`Game.java`](../src/main/java/com/mathwithmark/calculatorgamesolver/brutesolver/Game.java): interface for any Game
      * [`Solver.java`](../src/main/java/com/mathwithmark/calculatorgamesolver/brutesolver/Solver.java): logic to solve abstract games
      * [`State.java`](../src/main/java/com/mathwithmark/calculatorgamesolver/brutesolver/State.java): wrapper for Games that allows implicit linked lists of Games and transition messages
    * `calculatorgame`: a class for each rule in Calculator: The Game
      * [`CalculatorGame.java`](../src/main/java/com/mathwithmark/calculatorgamesolver/calculatorgame/CalculatorGame.java): the data structure for a game, including the logic to apply portals
      * [`Config.java`](../src/main/java/com/mathwithmark/calculatorgamesolver/calculatorgame/Config.java): configuration settings, prompts, all that jazz. This should be split into multiple files...
      * [`Helpers.java`](../src/main/java/com/mathwithmark/calculatorgamesolver/calculatorgame/Helpers.java): miscellaneous helper methods
    * `main`: the main classes of the program
      * [`Developer.java`](../src/main/java/com/mathwithmark/calculatorgamesolver/main/Developer.java): alternative main class with regression test factory
      * [`Main.java`](../src/main/java/com/mathwithmark/calculatorgamesolver/main/Main.java): I/O logic
    * `yaml`: Contains logic to serialize and deserialize with YAML
  * `test/java/com/mathwithmark/calculatorgamesolver`: test code
    * `calculatorgame`
      * [`LevelTests.java`](../src/test/java/com/mathwithmark/calculatorgamesolver/calculatorgame/LevelTests.java): tests for the ability to correctly solve levels
    * `main`
      * [`MainTests.java`](../src/test/java/com/mathwithmark/calculatorgamesolver/main/MainTests.java): comprehensive tests, from unit to end-to-end, covering nearly all functionality of the program
* `test-cases`: stores all test case files for the level-solver. File format found in the [testing document](./testing.md#test-cases)
