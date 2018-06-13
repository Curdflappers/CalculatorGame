package test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;
import main.*;

public class Test {
    public static void main(String[] args) {
        testRule();
        testGame();
        testMain();
        testState();
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
        // Test constructor
        Rule rule = new Rule("add1");
        Game game = new Game(1, 2, 1, new Rule[] {rule});
        assert game.getValue() == 1;
        assert game.getGoal() == 2;
        assert game.getMovesLeft() == 1;
        assert game.isValidRule(rule);
        assert !game.isValidRule(new Rule("subtract 1"));
        assert !game.isValidRule(new Rule("add 2"));
        assert !game.isValidRule(new Rule("subtract 2"));

        // Test makeMove
        game.makeMove(rule);
        assert game.getValue() == 2;
        assert game.getGoal() == 2;
        assert game.getMovesLeft() == 0;
        assert game.isValidRule(rule);

        System.out.println("testGame passed");
    }

    /**
     * Tests all methods within the Main class
     */
    private static void testMain() {
        testParseRules();
        testParseInput();
        testMainMethod();
        testState();
        System.out.println("testMain passed");
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
        System.out.println("testState passed");
        
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
        
        rule  = new Rule("subtract 1");
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
        
        System.out.println("testParse passed");
    }

    private static void testOperand() {
        Rule rule = new Rule("add 1");
        assert rule.getOperand() == 1;
        try {
            rule = new Rule("add 0");
            assert false; // exception should be thrown at this point
        } catch (RuntimeException e) { }
        try {
            rule = new Rule("add " + (Config.NUM_OPERANDS + 1));
            assert false;
        } catch (RuntimeException e) { }
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

    private static void testMainMethod() {
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
}
