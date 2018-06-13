package test;

import java.util.Scanner;
import main.*;

public class Test {
    public static void main(String[] args) {
        testRule();
        testGame();
        testMain();
        System.out.println("SUCCESS: All tests passed");
    }

    /**
     * Test all methods within the Rule class
     */
    private static void testRule() {
        testParse();
    }

    /**
     * Test all methods within the Game class
     */
    private static void testGame() {
        // Test constructor
        Rule rule = new Rule("add1");
        Game game = new Game(1, 2, 1, new Rule[] {rule});
        assert game.getState() == 1;
        assert game.getGoal() == 2;
        assert game.getMovesLeft() == 1;
        assert game.isValidRule(rule);
        assert !game.isValidRule(new Rule("subtract 1"));
        assert !game.isValidRule(new Rule("add 2"));
        assert !game.isValidRule(new Rule("subtract 2"));
        
        // Test makeMove
        game.makeMove(rule);
        assert game.getState() == 2;
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
        System.out.println("testMain passed");
    }
    
    /**
     * Test the Rule.parse operation
     */
    private static void testParse() {
        // Test constructor
        Rule rule = new Rule("add1");
        assert rule.getOperator() == Config.ADD;
        assert rule.getOperand() == 1;

        // Test synonyms
        rule = new Rule("+1");
        assert rule.getOperator() == Config.ADD;

        rule = new Rule("plus 4");
        assert rule.getOperator() == Config.ADD;
        
        System.out.println("testParse passed");
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
        Main.parseInput(new Scanner("1 2 1 +1,sub 2, add 1 "));
        assert Main.getState() == 1;
        assert Main.getGoal() == 2;
        assert Main.getMoves() == 1;
    }
}
