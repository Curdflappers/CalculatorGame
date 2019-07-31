# Testing

This document covers running tests.

This program was developed on Windows 10.

Testing is done with JUnit 5 and comprehensive unit tests and integration tests are found in `src/test/**/*.java`. All tests should pass.

## Running the Tests

1. Run `mvn test`. If anything goes wrong, try `mvn clean && mvn test`.

## Level Tests: Test Cases

Test cases for level-solving are stored in the `test-cases` directory. The files are named with a 3-digit code indicating their 1-based level index and are saved in YAML. Adding a test case file automatically means it will be tested: all files in the `/test-cases` directory are treated as test case files.

## Developer Mode

Running the program in developer mode allows the user to automatically write new test cases. Once a level is solved, the user is prompted to save the level as a test case. If they choose to do so, they can then enter a filename. A file under the given name is then saved in the `/test-cases` directory with the file extension `.yaml`. These test cases are automatically tested in [`LevelTests.java`](/src/test/java/com/mathwithmark/calculatorgamesolver/calculatorgame/LevelTests.java).
