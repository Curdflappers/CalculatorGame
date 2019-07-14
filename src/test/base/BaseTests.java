package base;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
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
    int[] portals;

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

        CalculatorGame game = new CalculatorGame(
            value,
            goal,
            movesLeft,
            validRules,
            null
        );

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

    /** Generates a basic instance of CalculatorGame to stay DRY */
    // TODO better way to stay DRY with JUnit-specific logic or final variable?
    private CalculatorGame calculatorGame() {
        Rule[] rules = new Rule[] {
            Rule.makeRule(Config.SIGN)
        };
        int value = 1, goal = 2, movesLeft = 3;
        return new CalculatorGame(
            value,
            goal,
            movesLeft,
            rules,
            null
        );
    }

    /** Generates a basic instance of CalculatorGame to stay DRY */
    private State state() {
        return new State(calculatorGame());
    }

    private String transitionString() {
        return "";
    }

    @Test
    void stateConstructorGame() {
        State sut = state();
        assertEquals(calculatorGame(), sut.getGame());
        assertNull(sut.getParent());
        assertNull(sut.getTransitionString());
    }

    void stateConstructorParentGameTransitionString() {
        State parent = state();
        State sut = new State(calculatorGame(), parent, transitionString());
        assertEquals(calculatorGame(), sut);
        assertEquals(parent, sut.getParent());
        assertEquals(transitionString(), sut.getTransitionString());
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
        boolean portalsPresent = false;
        String inputString =
            value
                + "\n"
                + goal
                + "\n"
                + movesLeft
                + "\n"
                + rulesString
                + "\n"
                + (portalsPresent ? "y" : "n")
                + "\n";

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
        int[] portals = {
            1, 0
        };
        assertCreatesGame(value, goal, moves, ruleStrings, portals);
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
        String[] ruleStrings,
        int[] portals
    ) {
        String inputString =
            inputString(false, value, goal, moves, ruleStrings, portals);
        InputStream in = inStream(inputString);
        InputStream consoleIn = System.in;
        Rule[] inputRules = rules(ruleStrings);
        CalculatorGame expectedGame = new CalculatorGame(
            value,
            goal,
            moves,
            inputRules,
            portals
        );

        // Creates game as user input
        Scanner scanner = new Scanner(in);
        Main.getInput(new String[0], scanner); // with no vm args
        assertEquals(expectedGame, Main.getCalculatorGame());

        // Creates game as VM args
        System.setIn(inStream(Config.QUIT));
        Main.main(inputStrings(value, goal, moves, ruleStrings, portals));
        assertEquals(expectedGame, Main.getCalculatorGame());

        System.setIn(consoleIn);
    }

    void assertMainAgainWorks(
        int initialValue,
        int goal,
        int moves,
        Rule[] rules,
        Rule[] solution,
        boolean[] apply
    ) {

        String[] ruleStrings = ruleStrings(rules);
        PrintStream out = System.out;
        String inputString =
            inputString(true, initialValue, goal, moves, ruleStrings, null);
        ByteArrayOutputStream baos = prepareEndToEndTest(inputString);
        Main.main(inputStrings(initialValue, goal, moves, ruleStrings, null));
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
            + Config.RULES_PROMPT
            + Config.PORTALS_PRESENT_PROMPT;
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
        String[] ruleStrings,
        int[] portals
    ) {

        String input = repeated ? Config.CONTINUE + "\n" : "";
        input +=
            value
                + "\n"
                + goal
                + "\n"
                + moves
                + "\n"
                + combineStrings(ruleStrings)
                + "\n\n";

        if (portals == null) {
            input += "n" + "\n";
        } else {
            input += "y" + "\n";
            input += String.valueOf(portals[0]) + "\n";
            input += String.valueOf(portals[1]) + "\n";
        }

        input += Config.QUIT;

        return input;
    }

    /** Simulates VM args */
    String[] inputStrings(
        int value,
        int goal,
        int moves,
        String[] ruleStrings,
        int[] portals
    ) {

        String rulesString =
            combineStrings(ruleStrings, Config.CMDLINE_SEPARATOR);
        boolean portalsPresent = portals != null;
        String portalsResponse = portalsPresent ? "y" : "n";
        String leftPortal = portalsPresent ? String.valueOf(portals[0]) : "";
        String rightPortal = portalsPresent ? String.valueOf(portals[1]) : "";
        return new String[] {
            "" + value,
            "" + goal,
            "" + moves,
            rulesString,
            portalsResponse,
            leftPortal,
            rightPortal
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
        String output = Config.SOLUTION_PROMPT + "\n";
        for (int i = 0; i < solution.length; i++) {
            Rule rule = solution[i];
            output +=
                (apply[i] ? Config.APPLY_PROMPT : Config.UPDATE_PROMPT)
                    + rule.toString()
                    + "\n";
        }
        output += Config.AGAIN_PROMPT;
        return output;
    }

    String stringFromBaos(ByteArrayOutputStream baos) {
        String rawOutput =
            new String(baos.toByteArray(), StandardCharsets.UTF_8);
        return rawOutput.replace("\r", "");
    }
}
