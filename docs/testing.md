# Testing

This document primarily covers running tests.

This program was primarily developed on Ubuntu 18.04, but it's also tested to work on Windows 10. As it was also developed with Visual Studio Code, testing instructions will be given for that IDE.

Testing is done with JUnit 5 and comprehensive unit tests and integration tests are found in `src/test/**/*.java`. All tests should pass.

## Running the Tests

1. Open the `Test` view from the ribbon on the side bar by clicking the titration flask icon.
1. Click the triangle (the `Run Test` button) at the top of the `Test` view.
1. The tests should run and complete within a few seconds (progress is recorded on the bottom status bar, near the left-hand side)
1. One the bottom status bar, there will be an `✗` followed by a number, then a `✓` followed by a number. If the number following the `✗` is `0`, then all tests passed. Click there (`View test report`) to see details on which tests failed and why.

There should be no unusual behavior in building or running the tests on any supported operating system.
