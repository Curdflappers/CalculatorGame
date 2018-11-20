# Calculator Game
A solver for the **Calculator: The Game** by Simple Machine. It runs through the terminal.

## Usage

Pass in the details of the level through the terminal, and the solution will be printed to standard output.

### Rules

Rules are comma-separated, no spaces in the separator please. Brackets (`[` and `]`) are used for visual purposes, do not include them in your input

* Add (`+[op1]`) adds `op1` to `value`
* Subtract (`-[op1]`) subtracts `op1` from `value`
* Multiply (`*[op1]`) multiplies `value` by `op1`
* Divide (`/[op1]`) divides `value` by `op1`
    * Dividing only works if `op1` divides `value`
* Pad (`[op1]`, "pad `op1`") adds `op1` to the right of value
    * 1 pad 0 becomes 10
    * 1 pad 2 becomes 12
    * 12 pad 34 becomes 1234
    * Padding only works if `op1` is nonnegative
* Sign (`+/-`) changes the sign of `value`
* Delete (`<<`) deletes the rightmost digit of `value`
    * 1234 delete becomes 123
    * -1234 delete becomes -123
    * 5 delete becomes 0
    * 0 delete becomes 0 (no change occurs)
* Convert (`[op1]=>[op2]`, "convert `op1` to `op2`") converts all instances of `op1` to `op2`
    * 1234 convert 4 to 5 becomes 1235
    * 1234 convert 34 to 89 becomes 1289
    * 123234 convert 23 to 5 becomes 1554
    * 1234 convert 5 to 6 becomes 1234 (no change occurs)
* Power (`^[op1]` "raised to the power of `op1`") raises `value` to the power of `op1`
    * 2 raised to the power of 3 becomes 8
* Reverse (`Reverse`) reverses the order of the digits in `value`
    * 1234 becomes 4321
    * -1234 becomes -4321
    * 1 becomes 1 (no change occurs)
* Sum (`SUM`) changes `value` into the sum of its digits, not changing the sign of `value`
    * 123 SUM becomes 6 (because 1 + 2 + 3 = 6)
    * -123 SUM becomes -6 (the sign will not change)
* Shift left (`< Shift`) and shift right (`Shift >`) shift the digits of value one position left or right, respectively
    * 1234 shift left becomes 2341
    * 1234 shift right becomes 4123

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
