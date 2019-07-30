# Testing

This document covers running tests.

This program was developed on Windows 10.

Testing is done with JUnit 5 and comprehensive unit tests and integration tests are found in `src/test/**/*.java`. All tests should pass.

## Running the Tests

1. Run `mvn test`

## Level Tests: Test Cases

Test cases for level-solving are stored in the `test-cases` directory. The files are named with a 3-digit code indicating their 1-based level index and always have the filename extension `.cgl`, which stands for "**C**alculator **G**ame **L**evel".

The files are of the following format:

```txt
<value>
<goal>
<moves>
<rule1>
<rule2>
...
<ruleN>

<portals present: 'y' or 'n'>
<leftPortalPosition (if present)>
<rightPortalPosition (if present)>
<solutionStep1>
<solutionStep2>
...
<solutionStepM>
```

Adding a test case file automatically means it will be tested: all files in the `/test-cases` directory are treated as test case files.

## Developer Mode

Running the program in developer mode allows the user to automatically write new test cases. Once a level is solved, the user is prompted to save the level as a test case. If they choose to do so, they can then enter a filename. A file under the given name is then saved in the `/test-cases` directory with the file extension `.cgl`. These test cases are automatically tested in [`LevelTests.java`](/src/test/java/com/mathwithmark/calculatorgamesolver/calculatorgame/LevelTests.java).

Developer Mode runs in an infinite loop: the program must be manually quit by some outside signal (e.g. clicking the X, using Alt+F4). This choice was made to speed up the creation of test cases.
