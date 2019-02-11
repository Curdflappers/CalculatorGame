package base;

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

import rules.Rule;

import org.junit.jupiter.api.Test;

public class BaseTests {
    Rule[] rules, solution;
    boolean[] apply;

    //////////////////
    // CONSTRUCTORS //
    //////////////////

    @Test
    void gameConstructor() {
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
    void stateConstructors() {
        Rule[] rules = new Rule[] {
            Rule.makeRule(Config.SIGN)
        };
        Rule rule = rules[0];
        int value = 1, goal = 2, movesLeft = 3;
        Game game = new Game(value, goal, movesLeft, rules);

        State parentState = new State(game);
        State childState = new State(parentState, rule, true);

        assertEquals(null, parentState.getRule());
        assertEquals(value, parentState.getValue());
        assertEquals(goal, parentState.getGoal());
        assertEquals(movesLeft, parentState.getMovesLeft());
        assertEquals(null, parentState.getParent());
        assertEquals(rule, childState.getRule());
        assertEquals(rule.apply(parentState.getGame()), childState.getGame());
        assertEquals(parentState, childState.getParent());
    }

    //////////
    // MAIN //
    //////////

    @Test
    void parseInput() {
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
        List<Rule> parsedRulesList = Arrays.asList(Main.getRules());
        assertEquals(rules.length, parsedRulesList.size());
        for (Rule rule : rules) {
            assertTrue(parsedRulesList.contains(rule));
        }
    }

    /**
     * Tests integration within Main by ensuring the game is set up correctly
     */
    @Test
    void mainCreatesGame() {
        int value = 1, goal = 2, moves = 3;
        String[] ruleStrings = {
            Config.ruleString(Config.ADD, 1),
            Config.ruleString(Config.SUBTRACT, 2),
            Config.ruleString(Config.MULTIPLY, 3),
        };
        assertCreatesGame(value, goal, moves, ruleStrings);
    }

    ////////////
    // SOLVES //
    ////////////

    /**
     * Level 1: Go from 1 to 3 using "+1" twice
     */
    @Test
    void solvesLevel1() {
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
    }

    /**
     * Level 4: 3 to 4 using *4, +4, /4 in three moves (in that order)
     */
    @Test
    void solvesLevel4() {
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
    }

    /**
     * Padding test: Go from 3 to 34 using pad4
     */
    @Test
    void solvesPad() {

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
    }

    /**
     * Delete test: go from 4321 to 4 using delete three times
     */
    @Test
    void solvesDelete() {
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
    }

    /**
     * Convert test: 0 to 222 using 1 and 1=>2
     */
    @Test
    void solvesPadAndConvert() {
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
    }

    /**
     * Level 118: From 2152 to 13 in 6 moves
     * Rules: 25=>12, 21=>3, 12=>5, Shift >, Reverse
     *
     */
    @Test
    void solvesLevel118() {
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
    }

    /**
     * Level 120: From 23 to 2332 in 1 move using Mirror
     */
    @Test
    void solvesLevel120() {
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
    }

    /**
     * Level 137: From 0 to 42 in 5 moves, including meta add 1
     * [+]1, +6, +6, *3, +6
     */
    @Test
    void solvesLevel137() {
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
    }

    /**
     * Level 146: 23 to 1234 in 4 moves including Store
     */
    @Test
    void solvesLevel146() {
        final Rule multiply2 = Rule.makeRule(Config.MULTIPLY, 2);
        final Rule subtract5 = Rule.makeRule(Config.SUBTRACT, 5);
        final Rule store = Rule.makeRule(Config.STORE);
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
    }

    /**
     * Level 147: 125 to 1025 in 6 moves
     *
     * May find repeat "Update Store"s, test to make sure these aren't
     * displayed in solution
     */
    @Test
    void solvesLevel147() {
        final Rule multiply2 = Rule.makeRule(Config.MULTIPLY, 2);
        final Rule store = Rule.makeRule(Config.STORE);
        final Rule delete = Rule.makeRule(Config.DELETE);

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
     * Uses Inverse Ten
     */
    @Test
    void solvesLevel162() {
        final Rule multiply3 = Rule.makeRule(Config.MULTIPLY, 3);
        final Rule divide9 = Rule.makeRule(Config.DIVIDE, 9);
        final Rule store = Rule.makeRule(Config.STORE);
        final Rule inverseTen = Rule.makeRule(Config.INVERSE_TEN);

        rules = new Rule[] {
            multiply3, divide9, store, inverseTen
        };
        solution = new Rule[] {
            store, store, multiply3, inverseTen, divide9
        };
        apply = new boolean[] {
            false, true, true, true, true, true, true
        };

        assertFindsSolution(5, 105, 6, rules, solution, apply);

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

    // --------//
    // Helpers //
    // --------//

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
