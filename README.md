# Calculator Game
A solver for **Calculator: The Game** by Simple Machine. It supports all 16 rule types. It supports portals. It can solve all 199 levels of the game.

It runs through the terminal. The solver prompts for input about a particular level, and outputs a series of steps to complete the given level. It then prompts the user to complete another level or quit the program. This repeats until the user chooses to quit the program.

This document covers the rules of Calculator: The Game and how to use the program. For installation instructions, see the [further documentation](./docs/index.md). If you have any issues with the program (installation or otherwise), feel free to open an issue on the repository.

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

### Portals

Portals are a complexifying mechanic in the game. Some games have portals: a pair of them, to be exact. One portal sits below the value, another floats above it. Each portal corresponds to exactly one position in the game's value, the ones position, tens positions, hundreds position, etc. The portal on the bottom will always be to the right of the portal on the top.

When the value of the game is such that there is a digit directly above the bottom portal, that digit falls through the bottom portal and is re-added to the value through the right portal. For example, if the portals are represented with a `Y`:

```
  Y
 12
Y
```

Nothing happens, this game is **stable**. But if we were to pad 3 to this value:

```
  Y
123
Y
```

This game is unstable. Since the `1` is directly above the lower portal, it will fall:

```
  Y
 24
Y
```

It gets added to the `3`, so the new digit is `4`. If there was a carry (say, a `2` being added to a `9`), it would be used. This can result in a **cascade** (multiple iterations of falling before the game is stable):

```
  Y
991
Y

  Y
100
Y

  Y
  1
Y
```

If _multiple digits are to the left of the bottom portal_, they each fall individually:

```
   Y
1232
  Y

   Y
 125
  Y

   Y
  17
  Y

   Y
   8
  Y
```

In this game, portals are represented by their distance from the ones position. In the example above, the left portal is represented with the value `1` since it is one spot away from the ones position, and the right portal is represented with the value `0`.

### Sample Use Case

```
Enter start value: 2
Enter goal value: 5
Enter the number of moves: 3
Enter one rule per line (empty string to mark end of list):
+2
+1

Are there any portals for this game? (y/n): n
Solution:
Apply +1
Apply +2
Solve again (y/n): y
Enter start value: 3002
Enter goal value: 3507
Enter the number of moves: 5
Enter one rule per line (empty string to mark end of list):
7
Shift >

Are there any portals for this game? (y/n): y
Enter the distance from the ones place of the portal on the left: 5
Enter the distance from the ones place of the portal on the right: 0
Solution:
Apply 7
Apply Shift >
Apply 7
Apply 7
Apply 7

```

#### Explanation

To go from 2 to 5 in at most 3 moves using the rules "add 2" and "add 1", a solution is to first add 1, then add 2. Although 3 moves are allowed, only 2 moves are needed. Other solutions exist, but this one is the first one found by the program, so it is the one output.

Let's examine the second case (going from 3002 to 3507):

```
     Y
  3002
Y

Apply 7
     Y
 30027
Y

Apply Shift >
     Y
 73002
Y

Apply 7
     Y
730027
Y

     Y
 30034
Y

Apply 7
     Y
300347
Y

     Y
   350
Y

Apply 7
     Y
  3507
Y
```

The program exits when user input after the `Solve again` prompt is not `'y'`.

## Roadmap

This program is enough to beat the original game. I want to do more.

Eventually, I'd like to:

* Add a feature to only display hints on demand
* Add a good UI (and restrict bad input)
* Be able to play the game
* Generate my own levels
* Create instructions
* Share levels with friends

## Further Documentation

All further documentation is found in the `docs` folder, with a convenient index [here](./docs/index.md).
