package com.example.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

public class AllTests {
    @Test
    void testRuleConstructor() {
        int operand1 = 1;
        int operand2 = 2;
        String ruleString = null;

        // Test all operator strings
        for (int i = 0; i < Config.OPERATOR_STRINGS.length; i++) {
            switch (Config.NUM_OPERANDS[i]) {
                case 0:
                    ruleString = Config.ruleString(i);
                    assertStringCreatesRule(ruleString, i);
                    break;
                case 1:
                    ruleString = Config.ruleString(i, operand1);
                    assertStringCreatesRule(ruleString, i, operand1);
                    break;
                case 2:
                    ruleString = Config.ruleString(i, operand1, operand2);
                    assertStringCreatesRule(ruleString, i, operand1, operand2);
                    break;
            }
        }

        // "*-2" used to parse as "*-", leading to invalid operator
        operand1 = -2; // or any negative number
        // note the lack of space between operator and operand
        ruleString = Config.ruleString(Config.MULTIPLY, operand1);
        assertStringCreatesRule(ruleString, Config.MULTIPLY, operand1);
    }

    /**
     * Asserts that the given string creates a rule with the given operator and
     * that both operands are 0
     */
    void assertStringCreatesRule(String str, int operator) {
        assertStringCreatesRule(str, operator, 0, 0);
    }

    /**
     * Asserts that the given string creates a rule with the given operator and
     * that both operands are 0
     */
    void assertStringCreatesRule(String str, int operator, int operand1) {
        assertStringCreatesRule(str, operator, operand1, 0);
    }

    void assertStringCreatesRule(
        String str,
        int operator,
        int operand1,
        int operand2
    ) {
        Rule rule = Rule.ruleFromString(str);
        assertEquals(Rule.makeRule(operator, operand1, operand2), rule);
        assertEquals(str, rule.toString());
    }

    /**
     * Tries to create an ADD rule with the given operand
     */
    Rule addOperand(int operand1) {
        return Rule.makeRule(Config.ADD, operand1);
    }

    @Test
    void testStateConstructors() {
        Rule[] rules = new Rule[] {
            Rule.makeRule(Config.SIGN)
        };
        Rule rule = rules[0];
        int value = 1, goal = 2, movesLeft = 3;
        Game game = new Game(value, goal, movesLeft, rules);
        State parentState = new State(game);
        assertEquals(null, parentState.getRule());
        assertEquals(value, parentState.getValue());
        assertEquals(goal, parentState.getGoal());
        assertEquals(movesLeft, parentState.getMovesLeft());
        assertEquals(null, parentState.getParent());

        State childState = new State(parentState, rule, true);
        assertEquals(rule, childState.getRule());
        assertEquals(rule.apply(parentState.getGame()), childState.getGame());
        assertEquals(parentState, childState.getParent());
    }

    @Test
    void testGameConstructor() {
        Rule[] validRules = {
            Rule.makeRule(Config.ADD, 1),
            Rule.makeRule(Config.SIGN),
            Rule.makeRule(Config.MULTIPLY, 2),
        };
        Rule[] invalidRules = {
            Rule.makeRule(Config.ADD, 2),
            Rule.makeRule(Config.REVERSE),
            Rule.makeRule(Config.MULTIPLY, -2),
        };
        int value = 1, goal = 2, movesLeft = 3;
        Game game = new Game(value, goal, movesLeft, validRules);
        assertEquals(value, game.getValue());
        assertEquals(goal, game.getGoal());
        assertEquals(movesLeft, game.getMovesLeft());
        for (Rule rule : validRules) {
            assertTrue(game.isValidRule(rule));
        }
        for (Rule rule : invalidRules) {
            assertFalse(game.isValidRule(rule));
        }
    }

    @Test
    void testApplyRule() {
        // Add
        assertApplyRule(3, Config.ADD, 2, 1);
        assertApplyRule(-3, Config.ADD, 4, -7);

        // Subtract
        assertApplyRule(3, Config.SUBTRACT, 4, 7);
        assertApplyRule(-3, Config.SUBTRACT, 5, 2);

        // Multiply
        assertApplyRule(12, Config.MULTIPLY, 3, 4);
        assertApplyRule(-27, Config.MULTIPLY, -9, 3);

        // Divide
        assertApplyRule(353, Config.DIVIDE, 2, 706);
        assertApplyRule(-535, Config.DIVIDE, -3, 535 * 3);

        // Pad
        assertApplyRule(12, Config.PAD, 2, 1);
        assertApplyRule(1210, Config.PAD, 10, 12);
        assertApplyRule(3, Config.PAD, 3, 0);
        assertApplyRule(30, Config.PAD, 0, 3);

        // Sign
        assertApplyRule(-1, Config.SIGN, 1);
        assertApplyRule(1, Config.SIGN, -1);
        assertApplyRule(-0, Config.SIGN, 0);

        // Delete
        assertApplyRule(1, Config.DELETE, 12);
        assertApplyRule(0, Config.DELETE, 1);
        assertApplyRule(0, Config.DELETE, 0);
        assertApplyRule(-1, Config.DELETE, -12);
        assertApplyRule(0, Config.DELETE, -1);
        assertApplyRule(0, Config.DELETE, 0);

        // Convert
        assertApplyRule(3, Config.CONVERT, 5, 3, 5);
        assertApplyRule(12, Config.CONVERT, 3, 1, 32);
        assertApplyRule(236523, Config.CONVERT, 56, 23, 566556);
        assertApplyRule(1, Config.CONVERT, 2, 3, 1);
        assertApplyRule(-3, Config.CONVERT, 5, 3, -5);
        assertApplyRule(-12, Config.CONVERT, 3, 1, -32);
        assertApplyRule(-236523, Config.CONVERT, 56, 23, -566556);
        assertApplyRule(-1, Config.CONVERT, 2, 3, -1);

        // Power
        assertApplyRule(8, Config.POWER, 3, 2);

        // Reverse
        assertApplyRule(35, Config.REVERSE, 53);
        assertApplyRule(0, Config.REVERSE, 0);
        assertApplyRule(-12, Config.REVERSE, -21);

        // Sum
        assertApplyRule(1 + 2 + 3, Config.SUM, 123);
        assertApplyRule(0, Config.SUM, 0);

        // Shift right
        assertApplyRule(4123, Config.SHIFT_RIGHT, 1234);
        assertApplyRule(-4123, Config.SHIFT_RIGHT, -1234);
        assertApplyRule(-2, Config.SHIFT_RIGHT, -2);

        // Shift left
        assertApplyRule(2341, Config.SHIFT_LEFT, 1234);
        assertApplyRule(-2341, Config.SHIFT_LEFT, -1234);
        assertApplyRule(-2, Config.SHIFT_LEFT, -2);

        // Mirror
        assertApplyRule(2332, Config.MIRROR, 23);
        assertApplyRule(0, Config.MIRROR, 0);
        assertApplyRule(-11, Config.MIRROR, -1);

        // Meta add
        int addOperand = 1;
        int subtractOperand = 2;
        int metaAddOperand = 3;
        Rule metaAddRule = Rule.makeRule(Config.META_ADD, metaAddOperand);
        Rule[] rules = new Rule[] {
            Rule.makeRule(Config.ADD, addOperand),
            Rule.makeRule(Config.SUBTRACT, subtractOperand),
            metaAddRule,
        };
        Rule[] expectedRules = new Rule[] {
            Rule.makeRule(Config.ADD, addOperand + metaAddOperand),
            Rule.makeRule(Config.SUBTRACT, subtractOperand + metaAddOperand),
            metaAddRule,
        };
        assertApplyMetaRule(expectedRules, metaAddRule, rules);

        // Store
        assertApplyStoreRule(1, 1, 11);
        assertApplyStoreRule(1, -1, 1);
    }

    /**
     * Asserts that the result of applying the given non-meta rule to a game
     * with the given value results in the expected value
     */
    void assertApplyRule(int expected, int operator, int value) {
        Rule rule = Rule.makeRule(operator);
        assertApplyRule(expected, rule, value);
    }

    /**
     * Asserts that the result of applying the given non-meta rule to a game
     * with the given value results in the expected value
     */
    void assertApplyRule(int expected, int operator, int operand1, int value) {
        Rule rule = Rule.makeRule(operator, operand1);
        assertApplyRule(expected, rule, value);
    }

    /**
     * Asserts that the result of applying the given non-meta rule to a game
     * with the given value results in the expected value
     */
    void assertApplyRule(
        int expected,
        int operator,
        int operand1,
        int operand2,
        int value
    ) {
        Rule rule = Rule.makeRule(operator, operand1, operand2);
        assertApplyRule(expected, rule, value);
    }

    /**
     * Asserts that the result of applying the given non-meta rule to a game
     * with the given value results in the expected value
     */
    void assertApplyRule(int expected, Rule rule, int value) {
        Game originalGame = new Game(value, 0, 0, new Rule[] {});
        double newValue = rule.apply(originalGame).getValue();
        assertEquals(expected, newValue, 0.01, rule.toString());
    }

    /**
     * Asserts that a meta rule correctly affects the rules and not the value
     * @param expectedRules
     * @param rule
     * @param oldRules
     */
    void assertApplyMetaRule(Rule[] expectedRules, Rule rule, Rule[] oldRules) {
        double value = -1;
        int goal = -2;
        int moves = 9; // some value > 0

        Game oldGame = new Game(value, goal, moves, oldRules);
        Game newGame = rule.apply(oldGame);
        Game expectedGame = new Game(value, goal, moves - 1, expectedRules);
        assertEquals(expectedGame, newGame);
    }

    void assertApplyStoreRule(int gameValue, int operand1, int newValue) {
        StoreRule rule = new StoreRule(operand1);
        assertApplyRule(newValue, rule, gameValue);
    }

    @Test
    void testParseInput() {
        int value = 1, goal = 2, movesLeft = 3;
        String[] ruleStrings = {
            Config.ruleString(Config.ADD, 1),
            Config.ruleString(Config.SUBTRACT, 2),
            Config.ruleString(Config.MULTIPLY, -3),
        };
        String rulesString = combineStrings(ruleStrings) + "\n";
        Rule[] rules = rules(ruleStrings);
        String inputString =
            value + "\n" + goal + "\n" + movesLeft + "\n" + rulesString + "\n";
        Main.parseInput(new Scanner(inputString));
        assertTrue(Main.getValue() == value);
        assertTrue(Main.getGoal() == goal);
        assertTrue(Main.getMoves() == movesLeft);
        Rule[] parsedRules = Main.getRules();
        List<Rule> parsedRulesList = Arrays.asList(parsedRules);
        for (Rule rule : rules) {
            assertTrue(
                parsedRulesList.contains(rule),
                parsedRulesList + " contains " + rule.toString()
            );
        }
    }

    /**
     * Tests integration within Main by ensuring the game is set up correctly
     */
    @Test
    void testMainGame() {
        int value = 1, goal = 2, moves = 3;
        String[] ruleStrings = {
            Config.ruleString(Config.ADD, 1),
            Config.ruleString(Config.SUBTRACT, 2),
            Config.ruleString(Config.MULTIPLY, 3),
        };
        assertCreatesGame(value, goal, moves, ruleStrings);
    }

    @Test
    void testMainSolve() {
        Rule[] rules, solution;
        boolean[] apply;

        // Level 1: Go from 1 to 3 using "+1" twice
        final Rule add1 = Rule.makeRule(Config.ADD, 1);
        rules = new Rule[] {
            add1, Rule.makeRule(Config.ADD, 3)
        };
        solution = new Rule[] {
            add1, add1
        };
        apply = new boolean[] {
            true, true
        };
        assertFindsSolution(0, 2, 2, rules, solution, apply);

        // Level 4: 3 to 4 using *4, +4, /4 in three moves (in that order)
        final Rule multiply4 = Rule.makeRule(Config.MULTIPLY, 4);
        final Rule add4 = Rule.makeRule(Config.ADD, 4);
        final Rule divide4 = Rule.makeRule(Config.DIVIDE, 4);
        rules = new Rule[] {
            multiply4, add4, divide4
        };
        solution = new Rule[] {
            multiply4, add4, divide4
        };
        apply = new boolean[] {
            true, true, true
        };
        assertFindsSolution(3, 4, 3, rules, solution, apply);

        // Padding test: Go from 3 to 34 using pad4
        final Rule pad4 = Rule.makeRule(Config.PAD, 4);
        rules = new Rule[] {
            pad4
        };
        solution = new Rule[] {
            pad4
        };
        apply = new boolean[] {
            true
        };
        assertFindsSolution(3, 34, 1, rules, solution, apply);

        // Delete test: go from 4321 to 4 using delete three times
        final Rule delete = Rule.makeRule(Config.DELETE);
        rules = new Rule[] {
            delete
        };
        solution = new Rule[] {
            delete, delete, delete
        };
        apply = new boolean[] {
            true, true, true
        };
        assertFindsSolution(4321, 4, 3, rules, solution, apply);

        // Convert test: 0 to 222 using 1 and 1=>2
        final Rule pad1 = Rule.makeRule(Config.PAD, 1);
        final Rule conv1to2 = Rule.makeRule(Config.CONVERT, 1, 2);
        rules = new Rule[] {
            pad1, conv1to2
        };
        solution = new Rule[] {
            pad1, pad1, pad1, conv1to2
        };
        apply = new boolean[] {
            true, true, true, true
        };
        assertFindsSolution(0, 222, 4, rules, solution, apply);

        // Level 118: From 2152 to 13 in 6 moves
        // Rules: 25=>12, 21=>3, 12=>5, Shift >, Reverse
        final Rule conv25to12 = Rule.makeRule(Config.CONVERT, 25, 12);
        final Rule conv21to3 = Rule.makeRule(Config.CONVERT, 21, 3);
        final Rule conv12to5 = Rule.makeRule(Config.CONVERT, 12, 5);
        final Rule shiftRight = Rule.makeRule(Config.SHIFT_RIGHT);
        final Rule reverse = Rule.makeRule(Config.REVERSE);
        rules = new Rule[] {
            conv25to12, conv21to3, conv12to5, shiftRight, reverse
        };
        solution = new Rule[] {
            reverse, shiftRight, conv25to12, conv12to5, conv25to12, conv21to3
        };
        apply = new boolean[] {
            true, true, true, true, true, true
        };
        assertFindsSolution(2152, 13, 6, rules, solution, apply);

        // Level 120: From 23 to 2332 in 1 move using Mirror
        final Rule mirror = Rule.makeRule(Config.MIRROR);
        rules = new Rule[] {
            mirror
        };
        solution = new Rule[] {
            mirror
        };
        apply = new boolean[] {
            true
        };
        assertFindsSolution(23, 2332, 1, rules, solution, apply);

        // Level 137: From 0 to 42 in 5 moves, including meta add 1
        // [+]1, +6, +6, *3, +6
        final Rule subtract2 = Rule.makeRule(Config.SUBTRACT, 2);
        final Rule add5 = Rule.makeRule(Config.ADD, 5);
        final Rule multiply2 = Rule.makeRule(Config.MULTIPLY, 2);
        final Rule metaAdd1 = Rule.makeRule(Config.META_ADD, 1);
        final Rule add6 = Rule.makeRule(Config.ADD, 6);
        final Rule multiply3 = Rule.makeRule(Config.MULTIPLY, 3);
        rules = new Rule[] {
            subtract2, add5, multiply2, metaAdd1
        };
        solution = new Rule[] {
            metaAdd1, add6, add6, multiply3, add6
        };
        apply = new boolean[] {
            true, true, true, true, true
        };
        assertFindsSolution(0, 42, 5, rules, solution, apply);

        // Level 146: 23 to 1234 in 4 moves including Store
        final Rule store = Rule.makeRule(Config.STORE);
        final Rule subtract5 = Rule.makeRule(Config.SUBTRACT, 5);
        final Rule shiftLeft = Rule.makeRule(Config.SHIFT_LEFT);
        rules = new Rule[] {
            multiply2, subtract5, store, shiftLeft
        };
        solution = new Rule[] {
            store, multiply2, subtract5, store, shiftLeft
        };
        apply = new boolean[] {
            false, true, true, true, true
        };
        assertFindsSolution(23, 1234, 4, rules, solution, apply);

        // Level 147: 125 to 1025 in 6 moves
        // May find repeat "Update Store"s, test to make sure these aren't
        // displayed in solution
        rules = new Rule[] {
            multiply2, store, delete
        };
        solution = new Rule[] {
            multiply2, delete, store, multiply2, delete, multiply2, store
        };
        apply = new boolean[] {
            true, true, false, true, true, true, true
        };
        assertFindsSolution(125, 1025, 6, rules, solution, apply);
    }

    /**
     * Tests whether the "again" functionality works
     */
    @Test
    void testMainAgain() {
        int value = 1, goal = 2, moves = 1;
        Rule rule = Rule.makeRule(Config.ADD, 1);
        Rule[] rules = {
            rule
        };
        Rule[] solution = new Rule[] {
            rule
        };
        boolean[] apply = {
            true
        };
        assertMainAgainWorks(value, goal, moves, rules, solution, apply);
    }

    /////////////
    // Helpers //
    /////////////

    /** Asserts that the given parameters create the given game in Main.main */
    void assertCreatesGame(
        int value,
        int goal,
        int moves,
        String[] ruleStrings) {
        String inputString =
            inputString(false, value, goal, moves, ruleStrings);
        InputStream in = inStream(inputString);
        InputStream consoleIn = System.in;
        Rule[] inputRules = rules(ruleStrings);
        Game expectedGame = new Game(value, goal, moves, inputRules);

        // Creates game as user input
        Scanner scanner = new Scanner(in);
        Main.getInput(new String[0], scanner); // with no vm args
        assertEquals(expectedGame, Main.getGame());

        // Creates game as VM args
        System.setIn(inStream(Config.QUIT));
        Main.main(inputStrings(value, goal, moves, ruleStrings));
        assertEquals(expectedGame, Main.getGame());

        System.setIn(consoleIn);
    }

    void assertFindsSolution(
        int initialValue,
        int goal,
        int moves,
        Rule[] rules,
        Rule[] solution,
        boolean[] apply) {

        String[] ruleStrings = ruleStrings(rules);
        PrintStream out = System.out;
        ByteArrayOutputStream baos = prepareEndToEndTest(Config.QUIT);
        Main.main(inputStrings(initialValue, goal, moves, ruleStrings));
        String expectedOutput = solutionOutput(solution, apply);
        String actualOutput = stringFromBaos(baos);
        assertEquals(expectedOutput, actualOutput);
        System.setOut(out);
    }

    void assertMainAgainWorks(
        int initialValue,
        int goal,
        int moves,
        Rule[] rules,
        Rule[] solution,
        boolean[] apply) {

        String[] ruleStrings = ruleStrings(rules);
        PrintStream out = System.out;
        String inputString =
            inputString(true, initialValue, goal, moves, ruleStrings);
        ByteArrayOutputStream baos = prepareEndToEndTest(inputString);
        Main.main(inputStrings(initialValue, goal, moves, ruleStrings));
        String expectedOutput = solutionOutput(solution, apply);
        expectedOutput += gamePrompts();
        expectedOutput += solutionOutput(solution, apply);
        String actualOutput = stringFromBaos(baos);
        assertEquals(expectedOutput, actualOutput);
        System.setOut(out);
    }

    /** Combines the given strings, separated with Config.SEPARATOR */
    String combineStrings(String[] strings) {
        return combineStrings(strings, Config.SCANNER_SEPARATOR);
    }

    /**
     * Creates one string given the array of strings, separated by the given
     * separator
     */
    String combineStrings(String[] strings, String separator) {
        String combinedString = "";
        int endIndex = -1;
        for (String string : strings) {
            combinedString += string + separator;
        }
        endIndex = combinedString.length() - separator.length();
        combinedString = combinedString.substring(0, endIndex);
        return combinedString;
    }

    String gamePrompts() {
        return Config.START_PROMPT
            + Config.GOAL_PROMPT
            + Config.MOVES_PROMPT
            + Config.RULES_PROMPT;
    }

    /**
     * Creates a newline-separated input string from the given parameters
     *
     * @param repeated whether this is for a repeat run-through. If true,
     * prefixes string with Config.CONTINUE
     */
    String inputString(
        boolean repeated,
        int value,
        int goal,
        int moves,
        String[] ruleStrings) {

        String input = repeated ? Config.CONTINUE : "";
        return input
            + value
            + "\n"
            + goal
            + "\n"
            + moves
            + "\n"
            + combineStrings(ruleStrings) + "\n\n" + Config.QUIT;
    }

    /** Simulates VM args */
    String[] inputStrings(
        int value,
        int goal,
        int moves,
        String[] ruleStrings) {
        return new String[] {
            "" + value,
            "" + goal,
            "" + moves,
            combineStrings(ruleStrings, Config.CMDLINE_SEPARATOR),
        };
    }

    /** Creates an input stream from the given input string */
    InputStream inStream(String inputString) {
        return new ByteArrayInputStream(inputString.getBytes());
    }

    /**
     * Sets output to print to BAOS, sets input to {@code input}
     *
     * @param input the new input String
     * @return the BAOS to which output is printed
     */
    private ByteArrayOutputStream prepareEndToEndTest(String input) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setIn(inStream(input));
        System.setOut(ps);
        return baos;
    }

    /** Creates an array of rules from the given rule strings */
    Rule[] rules(String[] ruleStrings) {
        Rule[] rules = new Rule[ruleStrings.length];
        for (int i = 0; i < ruleStrings.length; i++) {
            rules[i] = Rule.ruleFromString(ruleStrings[i]);
        }
        return rules;
    }

    String[] ruleStrings(Rule[] rules) {
        String[] ruleStrings = new String[rules.length];
        for (int i = 0; i < rules.length; i++) {
            ruleStrings[i] = rules[i].toString();
        }
        return ruleStrings;
    }

    /**
     * Returns the output for the given solution
     * Includes again prompt
     */
    String solutionOutput(Rule[] solution, boolean[] apply) {
        String lineEnd = "\n"; // Toggle this for Windows/non-Windows machines
        String output = Config.SOLUTION_PROMPT + lineEnd;
        for (int i = 0; i < solution.length; i++) {
            Rule rule = solution[i];
            output +=
                (apply[i] ? Config.APPLY_PROMPT : Config.UPDATE_PROMPT)
                    + rule.toString()
                    + lineEnd;
        }
        output += Config.AGAIN_PROMPT;
        return output;
    }

    String stringFromBaos(ByteArrayOutputStream baos) {
        return new String(baos.toByteArray(), StandardCharsets.UTF_8);
    }
}
