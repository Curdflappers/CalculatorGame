package test;

import main.*;

public class Test {
    public static void main(String[] args) {
        testRule();
        testGame();
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
    
}
