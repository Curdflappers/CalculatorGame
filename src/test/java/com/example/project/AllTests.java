package com.example.project;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

public class AllTests {
    @Test
    void allTests() {
        testRule();
        testGame();
        testMain();
        testState();
    }

    /**
     * Test all methods within the Rule class
     */
    private static void testRule() {
        testOperator();
        testOperand();
    }

    /**
     * Test all methods within the Game class
     */
    private static void testGame() {
        testGameConstructor();
        testApplyRule();
    }

    private static void testGameConstructor() {
        Rule add1 = new Rule("add1");
        Rule timesNegative3 = new Rule("times -3");
        Game game = new Game(1, 2, 1, new Rule[] {add1, timesNegative3});
        assertTrue(game.getValue() == 1);
        assertTrue(game.getGoal() == 2);
        assertTrue(game.getMovesLeft() == 1);
        assertTrue(game.isValidRule(add1));
        assertTrue(!game.isValidRule(new Rule("subtract 1")));
        assertTrue(!game.isValidRule(new Rule("add 2")));
        assertTrue(!game.isValidRule(new Rule("subtract 2")));
    }

    private static void testApplyRule() {
        Rule pad2 = new Rule("2");
        Rule pad10 = new Rule("10");
        Rule sign = new Rule("sign");
        Rule delete = new Rule("<<");
        Rule sub1 = new Rule("sub1");
        Rule conv1to2 = new Rule("1=>2");
        Rule squared = new Rule("x^2");
        Rule reverse = new Rule("reverse");
        Game game = new Game(1, 2, Integer.MAX_VALUE, new Rule[] {pad2, pad10,
            sign, delete, conv1to2, squared, reverse});

        assertTrue(game.getValue() == 1);

        game.makeMove(pad2);
        assertTrue(game.getValue() == 12);

        game.makeMove(pad10);
        assertTrue(game.getValue() == 1210);

        game.makeMove(sign);
        assertTrue(game.getValue() == -1210);

        game.makeMove(sign);
        assertTrue(game.getValue() == 1210);

        game.makeMove(delete);
        assertTrue(game.getValue() == 121);

        game.makeMove(delete);
        game.makeMove(delete);
        game.makeMove(delete);
        assertTrue(game.getValue() == 0);

        game.makeMove(sub1);
        game.makeMove(delete);
        assertTrue(game.getValue() == 0);

        game.makeMove(pad10);

        assertTrue(game.getValue() == 10);
        game.makeMove(conv1to2);
        assertTrue(game.getValue() == 20);

        game.makeMove(squared);
        assertTrue(game.getValue() == 400);

        game.makeMove(reverse);
        assertTrue(game.getValue() == 4);
        game.makeMove(sign);
        game.makeMove(pad2);
        assertTrue(game.getValue() == -42);
        game.makeMove(reverse);
        assertTrue(game.getValue() == -24);

        assertTrue(Game.applyRule(new Rule("sum"), 1234) == 1 + 2 + 3 + 4);
        assertTrue(Game.applyRule(new Rule("sum"), -1234) == -1 + -2 + -3 + -4);
    }

    /**
     * Tests all methods within the Main class
     */
    private static void testMain() {
        testParseRules();
        testParseInput();
        testMainGame();
        testMainSolve();
        testMainAgain();
    }

    /**
     * Tests the constructor of the Move class
     */
    public static void testState() {
        State state = new State(new Rule("+1"), 1, 2, 3, null);
        assertTrue(state.getRule().getOperator() == Config.ADD);
        assertTrue(state.getRule().getOperand() == 1);
        assertTrue(state.getValue() == 1);
        assertTrue(state.getGoal() == 2);
        assertTrue(state.getMovesLeft() == 3);
        assertTrue(state.getParent() == null);

        State state2 = new State(null, 4, 5, 6, state);
        assertTrue(state2.getParent() == state);

        Rule rule = new Rule("+1");
        State state3 = new State(state, rule);
        assertTrue(state3.getValue() == state.getValue() + 1);
        assertTrue(state3.getRule() == rule);
    }

    /**
     * Test the Rule.parse operation
     */
    private static void testOperator() {
        Rule rule = new Rule("add 1");
        assertTrue(rule.getOperator() == Config.ADD);
        rule = new Rule("plus 1");
        assertTrue(rule.getOperator() == Config.ADD);
        rule = new Rule("+1");
        assertTrue(rule.getOperator() == Config.ADD);

        rule = new Rule("subtract 1");
        assertTrue(rule.getOperator() == Config.SUBTRACT);
        rule = new Rule("sub 1");
        assertTrue(rule.getOperator() == Config.SUBTRACT);
        rule = new Rule("minus 1");
        assertTrue(rule.getOperator() == Config.SUBTRACT);
        rule = new Rule("-1");
        assertTrue(rule.getOperator() == Config.SUBTRACT);

        rule = new Rule("multiply 2");
        assertTrue(rule.getOperator() == Config.MULTIPLY);
        rule = new Rule("mul 2");
        assertTrue(rule.getOperator() == Config.MULTIPLY);
        rule = new Rule("mult 2");
        assertTrue(rule.getOperator() == Config.MULTIPLY);
        rule = new Rule("multiply by 2");
        assertTrue(rule.getOperator() == Config.MULTIPLY);
        rule = new Rule("times 2");
        assertTrue(rule.getOperator() == Config.MULTIPLY);
        rule = new Rule("* 2");
        assertTrue(rule.getOperator() == Config.MULTIPLY);
        rule = new Rule("*-3");
        assertTrue(rule.getOperator() == Config.MULTIPLY);
        rule = new Rule("x2");
        assertTrue(rule.getOperator() == Config.MULTIPLY);

        rule = new Rule("divide 2");
        assertTrue(rule.getOperator() == Config.DIVIDE);
        rule = new Rule("div 2");
        assertTrue(rule.getOperator() == Config.DIVIDE);
        rule = new Rule("divide by 2");
        assertTrue(rule.getOperator() == Config.DIVIDE);
        rule = new Rule("/ 2");
        assertTrue(rule.getOperator() == Config.DIVIDE);

        rule = new Rule("3");
        assertTrue(rule.getOperator() == Config.PAD);

        rule = new Rule("sign");
        assertTrue(rule.getOperator() == Config.SIGN);
        rule = new Rule("+/-");
        assertTrue(rule.getOperator() == Config.SIGN);

        rule = new Rule("delete");
        assertTrue(rule.getOperator() == Config.DELETE);
        rule = new Rule("del");
        assertTrue(rule.getOperator() == Config.DELETE);
        rule = new Rule("shift");
        assertTrue(rule.getOperator() == Config.DELETE);
        rule = new Rule("rshift");
        assertTrue(rule.getOperator() == Config.DELETE);
        rule = new Rule("rightshift");
        assertTrue(rule.getOperator() == Config.DELETE);
        rule = new Rule("right shift");
        assertTrue(rule.getOperator() == Config.DELETE);
        rule = new Rule("<<");
        assertTrue(rule.getOperator() == Config.DELETE);

        rule = new Rule("1=>2");
        assertTrue(rule.getOperator() == Config.CONVERT);

        rule = new Rule("x^2");
        assertTrue(rule.getOperator() == Config.POWER);
        rule = new Rule("^3");
        assertTrue(rule.getOperator() == Config.POWER);

        rule = new Rule("reverse");
        assertTrue(rule.getOperator() == Config.REVERSE);
        rule = new Rule("rev");
        assertTrue(rule.getOperator() == Config.REVERSE);
        rule = new Rule("Reverse");
        assertTrue(rule.getOperator() == Config.REVERSE);

        rule = new Rule("sum");
        assertTrue(rule.getOperator() == Config.SUM);
    }

    private static void testOperand() {
        Rule rule = new Rule("add 1");
        assertTrue(rule.getOperand() == 1);
        try {
            rule = new Rule("add " + (Config.MIN_OPERAND - 1));
            assertTrue(false); // exception should be thrown at this point
        } catch (RuntimeException e) {
        }
        try {
            rule = new Rule("add " + (Config.MAX_OPERAND + 1));
            assertTrue(false);
        } catch (RuntimeException e) {
        }
        rule = new Rule("2");
        assertTrue(rule.getOperand() == 2);

        rule = new Rule("1=>2");
        assertTrue(rule.getOperand() == 1);
        assertTrue(rule.getOperand2() == 2);
    }

    private static void testParseRules() {
        Main.parseRules(new String[] {"add1", "+2", "sub 3"});
        Rule[] rules = Main.getRules();
        assertTrue(rules[0].getOperator() == Config.ADD);
        assertTrue(rules[0].getOperand() == 1);
        assertTrue(rules[1].getOperator() == Config.ADD);
        assertTrue(rules[1].getOperand() == 2);
        assertTrue(rules[2].getOperator() == Config.SUBTRACT);
        assertTrue(rules[2].getOperand() == 3);
    }

    private static void testParseInput() {
        Main.parseInput(new Scanner("1\n2\n1\n+1,sub 2, add 1 "));
        assertTrue(Main.getValue() == 1);
        assertTrue(Main.getGoal() == 2);
        assertTrue(Main.getMoves() == 1);
    }

    /**
     * Tests integration within Main by ensuring the game is set up correctly
     */
    private static void testMainGame() {
        InputStream in = new ByteArrayInputStream(
            "1\n2\n1\n+1,sub 2, add 1 \nn".getBytes());
        InputStream consoleIn = System.in;
        System.setIn(in);
        Main.main(new String[0]); // with no input
        Game game = Main.getGame();
        assertTrue(game.getValue() == 1);
        assertTrue(game.getGoal() == 2);
        assertTrue(game.getMovesLeft() == 1);
        for (Rule rule : Main.getRules()) {
            assertTrue(game.isValidRule(rule));
        }
        assertTrue(!game.isValidRule(new Rule("+2")));

        System.setIn(new ByteArrayInputStream("n ".getBytes()));
        Main.main(new String[] {"4", "3", "2", "+2,- 1,add3"});
        game = Main.getGame();
        assertTrue(game.getValue() == 4);
        assertTrue(game.getGoal() == 3);
        assertTrue(game.getMovesLeft() == 2);
        for (Rule rule : Main.getRules()) {
            assertTrue(game.isValidRule(rule));
        }
        assertTrue(!game.isValidRule(new Rule("+1")));

        System.setIn(consoleIn);
    }

    private static void testMainSolve() {
        PrintStream out = System.out;
        String lineEnd = "\r\n";
        String expectedOutput = "";
        String actualOutput = "";
        String notAgain = "n "; // input to not play again

        // Level 1: Go from 1 to 3 using "+1" twice
        ByteArrayOutputStream baos = prepareEndToEndTest(notAgain);
        Rule rule = new Rule("+1");
        Main.main(new String[] {"0", "2", "2", rule.toString() + ",+3"});
        expectedOutput +=
            rule.toString() + lineEnd + rule.toString() + lineEnd;
        actualOutput = new String(baos.toByteArray(), StandardCharsets.UTF_8);
        assertTrue(actualOutput.equals(expectedOutput + Config.AGAIN_PROMPT));

        // Level 4: 3 to 4 using *4, +4, /4 in three moves (in that order)
        baos = prepareEndToEndTest(notAgain);
        Main.main(new String[] {"3", "4", "3", "+4,*4,/4"});
        expectedOutput = "*4" + lineEnd + "+4" + lineEnd + "/4" + lineEnd;
        actualOutput = new String(baos.toByteArray(), StandardCharsets.UTF_8);
        assertTrue(actualOutput.equals(expectedOutput + Config.AGAIN_PROMPT));

        // Padding test: Go from 3 to 34 using pad4
        baos = prepareEndToEndTest(notAgain);
        Main.main(new String[] {"3", "34", "1", "4"});
        expectedOutput = "4" + lineEnd;
        actualOutput = new String(baos.toByteArray(), StandardCharsets.UTF_8);
        assertTrue(actualOutput.equals(expectedOutput + Config.AGAIN_PROMPT));

        // Delete test: go from 4321 to 4 using delete three times
        baos = prepareEndToEndTest(notAgain);
        Main.main(new String[] {"4321", "4", "3", "<<"});
        expectedOutput = "<<" + lineEnd + "<<" + lineEnd + "<<" + lineEnd;
        actualOutput = new String(baos.toByteArray(), StandardCharsets.UTF_8);
        assertTrue(actualOutput.equals(expectedOutput + Config.AGAIN_PROMPT));

        // Convert test: 0 to 222 using 1 and 1=>2
        baos = prepareEndToEndTest(notAgain);
        Main.main(new String[] {"0", "222", "4", "1,1=>2"});
        expectedOutput =
            "1" + lineEnd + "1" + lineEnd + "1" + lineEnd + "1=>2" + lineEnd;
        actualOutput = new String(baos.toByteArray(), StandardCharsets.UTF_8);
        assertTrue(actualOutput.equals(expectedOutput + Config.AGAIN_PROMPT));

        System.setOut(out);
    }

    /**
     * Tests whether the "again" functionality works
     */
    private static void testMainAgain() {
        ByteArrayOutputStream baos;
        String expectedOutput = "", lineEnd = "\r\n", actualOutput = "";

        // Padding test: Go from 3 to 34 using pad4
        baos = prepareEndToEndTest("y\n4321\n4\n3\n<<\nn\n");
        Main.main(new String[] {"3", "34", "1", "4"});
        expectedOutput = "4" + lineEnd + Config.AGAIN_PROMPT;
        expectedOutput += Config.START_PROMPT + Config.GOAL_PROMPT
            + Config.MOVES_PROMPT + Config.RULES_PROMPT;
        expectedOutput += "<<" + lineEnd + "<<" + lineEnd + "<<" + lineEnd;
        actualOutput = new String(baos.toByteArray(), StandardCharsets.UTF_8);
        assertTrue(actualOutput.equals(expectedOutput + Config.AGAIN_PROMPT));
    }

    /**
     * Sets output to print to BAOS, sets input to {@code input}
     * 
     * @param input the new input String
     * @return the BAOS to which output is printed
     */
    private static ByteArrayOutputStream prepareEndToEndTest(String input) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        System.setOut(ps);
        return baos;
    }
}
