package com.mathwithmark.calculatorgamesolver.main;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.mathwithmark.calculatorgamesolver.calculatorgame.CalculatorGame;
import com.mathwithmark.calculatorgamesolver.calculatorgame.Config;
import com.mathwithmark.calculatorgamesolver.calculatorgame.Helpers;
import com.mathwithmark.calculatorgamesolver.calculatorgame.Rule;

import org.junit.jupiter.api.Test;

import com.mathwithmark.calculatorgamesolver.brutesolver.State;

public class MainTests {
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

        CalculatorGame game =
            new CalculatorGame(value, goal, movesLeft, validRules, null);

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
    private CalculatorGame calculatorGame() {
        Rule[] rules = new Rule[] {
            Rule.makeRule(Config.SIGN)
        };
        int value = 1, goal = 2, movesLeft = 3;
        return new CalculatorGame(value, goal, movesLeft, rules, null);
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
        String rulesString = Helpers.combineStrings(ruleStrings, "\n") + "\n";
        Rule[] rules = Helpers.rules(ruleStrings);
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
        List<String> solution = new ArrayList<>();
        solution.add(CalculatorGame.transitionString(rule));
        assertMainAgainWorks(value, goal, moves, rules, solution);
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
        InputStream in = IoUtils.inStream(inputString);
        InputStream consoleIn = System.in;
        Rule[] inputRules = Helpers.rules(ruleStrings);
        CalculatorGame expectedGame =
            new CalculatorGame(value, goal, moves, inputRules, portals);

        // Creates game as user input
        Scanner scanner = new Scanner(in);
        Main.getInput(new String[0], scanner); // with no vm args
        assertEquals(expectedGame, Main.getCalculatorGame());

        // Creates game as VM args
        System.setIn(IoUtils.inStream(Config.QUIT));
        Main.main(TestUtils.args(value, goal, moves, ruleStrings, portals));
        assertEquals(expectedGame, Main.getCalculatorGame());

        System.setIn(consoleIn);
    }

    void assertMainAgainWorks(
        int initialValue,
        int goal,
        int moves,
        Rule[] rules,
        List<String> solution
    ) {
        String[] ruleStrings = Helpers.ruleStrings(rules);
        PrintStream out = System.out;
        String inputString =
            inputString(true, initialValue, goal, moves, ruleStrings, null);
        ByteArrayOutputStream baos = IoUtils.prepareEndToEndTest(inputString);
        String expectedOutput = solutionOutput(solution);
        expectedOutput += gamePrompts();
        expectedOutput += solutionOutput(solution);

        Main.main(TestUtils.args(initialValue, goal, moves, ruleStrings, null));
        String actualOutput = IoUtils.stringFromBaos(baos);

        assertEquals(expectedOutput, actualOutput);

        System.setOut(out);
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
                + Helpers.combineStrings(ruleStrings, "\n")
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

    /**
     * Returns the output for the given solution
     * Includes again prompt
     */
    String solutionOutput(List<String> solution) {
        String output = Main.solutionPrintString(solution);
        output += Config.AGAIN_PROMPT;
        return output;
    }
}
