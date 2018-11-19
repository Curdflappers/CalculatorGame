# Calculator Game
A solver for the **Calculator: The Game** by Simple Machine. It runs through the terminal.

## Usage

Pass in the details of the level through the terminal, and the solution will be printed to standard output.

### Sample Use Case

```
Enter start value: 2
Enter goal value: 5
Enter the number of moves: 3
Enter comma-separated rules: +2,+1
+1
+2
Solve again (y/n): y
Enter start value: 1231
Enter goal value: 4
Enter the number of moves: 3
Enter comma-separated rules: SUM,3=>1,2=>3
2=>3
3=>1
SUM
Solve again (y/n): n
```

## Testing

Testing is done with JUnit 5 and comprehensive unit tests and integration tests are found in `src/test/.../AllTests.java`. All tests should pass.

## Roadmap

I want to do more.

Eventually, I'd like to:

* Support all operators
* Add a feature to only display hints on demand
* Add a good UI (and restrict bad input)
* Be able to play the game
* Generate my own levels
* Create instructions
* Share levels with friends
