# Calculator Game
A solver for **Calculator: The Game** by Simple Machine. It runs through the terminal.

## Usage

Pass in the details of the level through the terminal, and the solution will be printed to standard output.

### Rules

Rules are newline-separated. Parentheses are used for visual purposes, do not include them in your input

* Add (`+(op1)`) adds `op1` to `value`
* Subtract (`-(op1)`) subtracts `op1` from `value`
* Multiply (`*(op1)`) multiplies `value` by `op1`
* Divide (`/(op1)`) divides `value` by `op1`
    * Dividing only works if `op1` divides `value`
* Pad (`(op1)`, "pad `op1`") adds `op1` to the right of value
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
* Convert (`(op1)=>(op2)`, "convert `op1` to `op2`") converts all instances of `op1` to `op2`
    * 1234 convert 4 to 5 becomes 1235
    * 1234 convert 34 to 89 becomes 1289
    * 123234 convert 23 to 5 becomes 1554
    * 1234 convert 5 to 6 becomes 1234 (no change occurs)
    * 123 convert 2 to 00 becomes 1003 (multiple zeros are supported)
* Power (`^(op1)` "raised to the power of `op1`") raises `value` to the power of `op1`
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
* Mirror (`Mirror`) appends the mirrored value to the end of `value`
    * 23 mirror becomes 2332
    * -1 mirror becomes -11
* Meta Add (`[+](op1)`) adds `op1` to each non-meta rule in the game
* Store (`Store`) is the first rule that can be updated.
    * The Store rule cannot be applied until it is updated.
    * Upon updating the Store rule, its operand becomes the value of the game
    * Applying the Store rule functions just like applying the Pad rule
    * The Store rule can be updated any number of times, and updating the Store rule does not decrease the move counter.
    * Applying the Store rule when its value is negative does nothing.
* Inverse Ten (`Inv10`) converts each digit to its "10-additive inverse"
    * 4 becomes 6, because 4 + 6 = 10
    * 123 becomes 987, because each digit is evaluated independently
    * 5 becomes 5 (no change occurs)
    * 0 becomes 0 (no change occurs)
    * -123 becomes -987 (the sign does not change)

### Sample Use Case

```
Enter start value: 2
Enter goal value: 5
Enter the number of moves: 3
Enter one rule per line (empty string to mark end of list):
+2
+1

Solution:
Apply +1
Apply +2
Solve again (y/n): y
Enter start value: 1231
Enter goal value: 4
Enter the number of moves: 3
Enter one rule per line (empty string to mark end of list):
SUM
3=>1
2=>3

Solution:
Apply 2=>3
Apply 3=>1
Apply SUM
Solve again (y/n): n
```

#### Explanation

To go from 2 to 5 in at most 3 moves using the rules "add 2" and "add 1", a solution is to first add 1, then add 2. Although 3 moves are allowed, only 2 moves are needed. Other solutions exist, but this one is the first one found by the program, so it is the one output.

To go from 1231 to 4 in at most 3 moves using the rules "sum", "convert 3 to 1", and "convert 2 to 3",  a solution is to first convert 2 to 3, then convert 3 to 1, then take the sum.

The program exits when user input after the `Solve again` prompt is not `'y'`.

## Testing

Testing is done with JUnit 5 and comprehensive unit tests and integration tests are found in `src/test/**/*.java`. All tests should pass.

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
