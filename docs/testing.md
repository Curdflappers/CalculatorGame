# Testing

This document primarily covers running tests.

This program was primarily developed on Ubuntu 18.04, but it's also tested to work on Windows 10. As it was also developed with Visual Studio Code, testing instructions will be given for that IDE.

Testing is done with JUnit 5 and comprehensive unit tests and integration tests are found in `src/test/**/*.java`. All tests should pass.

## Running the Tests

1. Open the `Test` view from the ribbon on the side bar by clicking the flask icon.
1. Click the triangle (the `Run Test` button) at the top of the `Test` view.
1. The tests should run and complete within a few seconds (progress is recorded on the bottom status bar, near the left-hand side)
1. One the bottom status bar, there will be an `✗` followed by a number, then a `✓` followed by a number. If the number following the `✗` is `0`, then all tests passed. Click there (`View test report`) to see details on which tests failed and why.

There should be no unusual behavior in building or running the tests on any supported operating system.

## Test Cases

Test cases for level-solving are stored in the `test-cases` directory. The files are usually named with a 3-digit code indicating their 1-based level index and always have the filename extension `.cgl`, which stands for "Calculator Game Level".

The files are of the following format:

```
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

Adding a test case file automatically means it will be tested: all files in that folder are treated as test case files, so don't add anything else to that folder.

### Developer Mode

Running the program in developer mode allows the user to automatically write new test cases. Once a level is solved, the user is prompted to save the test case. If they choose to do so, they can then enter a filename. A file under the given name is then saved in the `/test-cases` directory with the ending `.cgl`. These test case files are automatically tested in [`LevelTests.java`](/src/test/calculatorgame/LevelTests.java).

Developer Mode runs in an infinite loop: the program must be manually quit by some outside force (e.g. clicking the X, using Alt+F4). This choice was made to speed up the creation of test cases.
