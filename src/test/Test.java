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
        Rule rule = new Rule("add1");
        Game game = new Game(1, 2, 1, new Rule[] {rule});
        assert game.getValue() == 1;
        assert game.getGoal() == 2;
        assert game.getMovesLeft() == 1;
        assert game.isValidRule(rule);
        assert !game.isValidRule(new Rule("subtract 1"));
        assert !game.isValidRule(new Rule("add 2"));
        assert !game.isValidRule(new Rule("subtract 2"));
    }

    private static void testApplyRule() {
        Rule add1 = new Rule("+1");
        Rule sub1 = new Rule("-1");
        Rule mul2 = new Rule("*2");
        Rule div2 = new Rule("/2");
        Rule pad2 = new Rule("2");
        Game game =
            new Game(1, 2, 10, new Rule[] {add1, sub1, mul2, div2, pad2});

        game.makeMove(add1);
        assert game.getValue() == 2;
        assert game.getGoal() == 2;
        assert game.getMovesLeft() == 9;
        assert game.isValidRule(add1);

        game.makeMove(pad2);
        assert game.getValue() == 22;
    }

    /**
     * Tests all methods within the Main class
     */
    private static void testMain() {
        testParseRules();
        testParseInput();
        testMainGame();
        testMainSolve();
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
    }

    private static void testOperand() {
        Rule rule = new Rule("add 1");
        assert rule.getOperand() == 1;
        try {
            rule = new Rule("add 0");
            assert false; // exception should be thrown at this point
        } catch (RuntimeException e) {
        }
        try {
            rule = new Rule("add " + (Config.NUM_OPERANDS + 1));
            assert false;
        } catch (RuntimeException e) {
        }
        rule = new Rule("2");
        assert rule.getOperand() == 2;
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
        InputStream in =
            new ByteArrayInputStream("1\n2\n1\n+1,sub 2, add 1 ".getBytes());
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

        System.setIn(consoleIn);
        Main.main(new String[] {"4", "3", "2", "+2,-1,add3"});
        game = Main.getGame();
        assert game.getValue() == 4;
        assert game.getGoal() == 3;
        assert game.getMovesLeft() == 2;
        for (Rule rule : Main.getRules()) {
            assert game.isValidRule(rule);
        }
        assert !game.isValidRule(new Rule("+1"));
    }

    private static void testMainSolve() {
        PrintStream out = System.out;
        String lineEnd = "\r\n";
        String expectedOutput = "";
        String actualOutput = "";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setOut(ps);
        Rule rule = new Rule("+1");
        Main.main(new String[] {"0", "2", "2", rule.toString() + ",+3"});
        expectedOutput +=
            rule.toString() + lineEnd + rule.toString() + lineEnd;
        actualOutput = new String(baos.toByteArray(), StandardCharsets.UTF_8);
        assert actualOutput.equals(expectedOutput);

        baos = new ByteArrayOutputStream();
        ps = new PrintStream(baos);
        System.setOut(ps);
        Main.main(new String[] {"3", "4", "3", "+4,*4,/4"});
        expectedOutput = "*4" + lineEnd + "+4" + lineEnd + "/4" + lineEnd;
        actualOutput = new String(baos.toByteArray(), StandardCharsets.UTF_8);
        assert actualOutput.equals(expectedOutput);

        baos = new ByteArrayOutputStream();
        ps = new PrintStream(baos);
        System.setOut(ps);
        Main.main(new String[] {"3", "34", "1", "4"});
        expectedOutput = "4" + lineEnd;
        actualOutput = new String(baos.toByteArray(), StandardCharsets.UTF_8);
        assert actualOutput.equals(expectedOutput);

        System.setOut(out);
    }
}
