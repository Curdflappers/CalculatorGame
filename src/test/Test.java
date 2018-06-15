package test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import main.*;

public class Test {
    public static void main(String[] args) {
        PrintStream out = System.out;
        System.setOut(new PrintStream(new ByteArrayOutputStream()));
        Rule rule = new Rule("-3");
        testRule();
        testGame();
        testMain();
        testState();
        System.setOut(out);
        System.out.println("SUCCESS: All tests passed");
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
        assert game.getValue() == 1;
        assert game.getGoal() == 2;
        assert game.getMovesLeft() == 1;
        assert game.isValidRule(add1);
        assert !game.isValidRule(new Rule("subtract 1"));
        assert !game.isValidRule(new Rule("add 2"));
        assert !game.isValidRule(new Rule("subtract 2"));
    }

    private static void testApplyRule() {
        Rule pad2 = new Rule("2");
        Rule pad10 = new Rule("10");
        Rule sign = new Rule("sign");
        Rule delete = new Rule("<<");
        Rule sub1 = new Rule("sub1");
        Rule conv1to2 = new Rule("1=>2");
        Game game = new Game(1, 2, 10,
            new Rule[] {pad2, pad10, sign, delete, conv1to2});

        assert game.getValue() == 1;

        game.makeMove(pad2);
        assert game.getValue() == 12;

        game.makeMove(pad10);
        assert game.getValue() == 1210;

        game.makeMove(sign);
        assert game.getValue() == -1210;

        game.makeMove(sign);
        assert game.getValue() == 1210;

        game.makeMove(delete);
        assert game.getValue() == 121;

        game.makeMove(delete);
        game.makeMove(delete);
        game.makeMove(delete);
        assert game.getValue() == 0;

        game.makeMove(sub1);
        game.makeMove(delete);
        assert game.getValue() == 0;

        game.makeMove(pad10);

        assert game.getValue() == 10;
        game.makeMove(conv1to2);
        assert game.getValue() == 20;
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
        assert state.getRule().getOperator() == Config.ADD;
        assert state.getRule().getOperand() == 1;
        assert state.getValue() == 1;
        assert state.getGoal() == 2;
        assert state.getMovesLeft() == 3;
        assert state.getParent() == null;

        State state2 = new State(null, 4, 5, 6, state);
        assert state2.getParent() == state;

        Rule rule = new Rule("+1");
        State state3 = new State(state, rule);
        assert state3.getValue() == state.getValue() + 1;
        assert state3.getRule() == rule;
    }

    /**
     * Test the Rule.parse operation
     */
    private static void testOperator() {
        Rule rule = new Rule("add 1");
        assert rule.getOperator() == Config.ADD;
        rule = new Rule("plus 1");
        assert rule.getOperator() == Config.ADD;
        rule = new Rule("+1");
        assert rule.getOperator() == Config.ADD;

        rule = new Rule("subtract 1");
        assert rule.getOperator() == Config.SUBTRACT;
        rule = new Rule("sub 1");
        assert rule.getOperator() == Config.SUBTRACT;
        rule = new Rule("minus 1");
        assert rule.getOperator() == Config.SUBTRACT;
        rule = new Rule("-1");
        assert rule.getOperator() == Config.SUBTRACT;

        rule = new Rule("multiply 2");
        assert rule.getOperator() == Config.MULTIPLY;
        rule = new Rule("mul 2");
        assert rule.getOperator() == Config.MULTIPLY;
        rule = new Rule("mult 2");
        assert rule.getOperator() == Config.MULTIPLY;
        rule = new Rule("multiply by 2");
        assert rule.getOperator() == Config.MULTIPLY;
        rule = new Rule("times 2");
        assert rule.getOperator() == Config.MULTIPLY;
        rule = new Rule("* 2");
        assert rule.getOperator() == Config.MULTIPLY;
        rule = new Rule("*-3");
        assert rule.getOperator() == Config.MULTIPLY;

        rule = new Rule("divide 2");
        assert rule.getOperator() == Config.DIVIDE;
        rule = new Rule("div 2");
        assert rule.getOperator() == Config.DIVIDE;
        rule = new Rule("divide by 2");
        assert rule.getOperator() == Config.DIVIDE;
        rule = new Rule("/ 2");
        assert rule.getOperator() == Config.DIVIDE;

        rule = new Rule("3");
        assert rule.getOperator() == Config.PAD;

        rule = new Rule("sign");
        assert rule.getOperator() == Config.SIGN;

        rule = new Rule("delete");
        assert rule.getOperator() == Config.DELETE;
        rule = new Rule("del");
        assert rule.getOperator() == Config.DELETE;
        rule = new Rule("shift");
        assert rule.getOperator() == Config.DELETE;
        rule = new Rule("rshift");
        assert rule.getOperator() == Config.DELETE;
        rule = new Rule("rightshift");
        assert rule.getOperator() == Config.DELETE;
        rule = new Rule("right shift");
        assert rule.getOperator() == Config.DELETE;
        rule = new Rule("<<");
        assert rule.getOperator() == Config.DELETE;

        rule = new Rule("1=>2");
        assert rule.getOperator() == Config.CONVERT;
    }

    private static void testOperand() {
        Rule rule = new Rule("add 1");
        assert rule.getOperand() == 1;
        try {
            rule = new Rule("add " + (Config.MIN_OPERAND - 1));
            assert false; // exception should be thrown at this point
        } catch (RuntimeException e) {
        }
        try {
            rule = new Rule("add " + (Config.MAX_OPERAND + 1));
            assert false;
        } catch (RuntimeException e) {
        }
        rule = new Rule("2");
        assert rule.getOperand() == 2;

        rule = new Rule("1=>2");
        assert rule.getOperand() == 1;
        assert rule.getOperand2() == 2;
    }

    private static void testParseRules() {
        Main.parseRules(new String[] {"add1", "+2", "sub 3"});
        Rule[] rules = Main.getRules();
        assert rules[0].getOperator() == Config.ADD;
        assert rules[0].getOperand() == 1;
        assert rules[1].getOperator() == Config.ADD;
        assert rules[1].getOperand() == 2;
        assert rules[2].getOperator() == Config.SUBTRACT;
        assert rules[2].getOperand() == 3;
    }

    private static void testParseInput() {
        Main.parseInput(new Scanner("1\n2\n1\n+1,sub 2, add 1 "));
        assert Main.getValue() == 1;
        assert Main.getGoal() == 2;
        assert Main.getMoves() == 1;
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
        assert game.getValue() == 1;
        assert game.getGoal() == 2;
        assert game.getMovesLeft() == 1;
        for (Rule rule : Main.getRules()) {
            assert game.isValidRule(rule);
        }
        assert !game.isValidRule(new Rule("+2"));

        System.setIn(new ByteArrayInputStream("n ".getBytes()));
        Main.main(new String[] {"4", "3", "2", "+2,- 1,add3"});
        game = Main.getGame();
        assert game.getValue() == 4;
        assert game.getGoal() == 3;
        assert game.getMovesLeft() == 2;
        for (Rule rule : Main.getRules()) {
            assert game.isValidRule(rule);
        }
        assert !game.isValidRule(new Rule("+1"));

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
        assert actualOutput.equals(expectedOutput + Config.AGAIN_PROMPT);

        // Level 4: 3 to 4 using *4, +4, /4 in three moves (in that order)
        baos = prepareEndToEndTest(notAgain);
        Main.main(new String[] {"3", "4", "3", "+4,*4,/4"});
        expectedOutput = "*4" + lineEnd + "+4" + lineEnd + "/4" + lineEnd;
        actualOutput = new String(baos.toByteArray(), StandardCharsets.UTF_8);
        assert actualOutput.equals(expectedOutput + Config.AGAIN_PROMPT);

        // Padding test: Go from 3 to 34 using pad4
        baos = prepareEndToEndTest(notAgain);
        Main.main(new String[] {"3", "34", "1", "4"});
        expectedOutput = "4" + lineEnd;
        actualOutput = new String(baos.toByteArray(), StandardCharsets.UTF_8);
        assert actualOutput.equals(expectedOutput + Config.AGAIN_PROMPT);

        // Delete test: go from 4321 to 4 using delete three times
        baos = prepareEndToEndTest(notAgain);
        Main.main(new String[] {"4321", "4", "3", "<<"});
        expectedOutput = "<<" + lineEnd + "<<" + lineEnd + "<<" + lineEnd;
        actualOutput = new String(baos.toByteArray(), StandardCharsets.UTF_8);
        assert actualOutput.equals(expectedOutput + Config.AGAIN_PROMPT);

        // Convert test: 0 to 222 using 1 and 1=>2
        baos = prepareEndToEndTest(notAgain);
        Main.main(new String[] {"0", "222", "4", "1,1=>2"});
        expectedOutput =
            "1" + lineEnd + "1" + lineEnd + "1" + lineEnd + "1=>2" + lineEnd;
        actualOutput = new String(baos.toByteArray(), StandardCharsets.UTF_8);
        assert actualOutput.equals(expectedOutput + Config.AGAIN_PROMPT);

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
        assert actualOutput.equals(expectedOutput + Config.AGAIN_PROMPT);
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
