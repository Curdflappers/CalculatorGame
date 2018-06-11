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
        Rule rule = new Rule("add 1");
        Game game = new Game(1, 2, 1, new Rule[] {rule});
        assert game.getState() == 1;
        assert game.getGoal() == 2;
        assert game.getMovesLeft() == 1;
        assert game.isValidRule(rule);
        assert !game.isValidRule(new Rule("subtract 1"));
        assert !game.isValidRule(new Rule("add 2"));
        assert !game.isValidRule(new Rule("subtract 2"));
        
        System.out.println("testGame passed");
    }
    
    /**
     * Test the Rule.parse operation
     */
    private static void testParse() {
        Rule rule = new Rule("add 1");
        assert rule.getOperator() == Config.ADD;
        assert rule.getOperand() == 1;

        rule = new Rule("+ 1");
        assert rule.getOperator() == Config.ADD;

        rule = new Rule("plus 4");
        assert rule.getOperator() == Config.ADD;
        
        System.out.println("testParse passed");
    }
    
}
