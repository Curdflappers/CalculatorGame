package main;

public class Game {
    /** The current number for this game */
    private int value;
    
    /** The goal number for this game */
    private int goal;
    
    /** The moves left in this game */
    private int movesLeft;
    
    /** The rules that can be used in this game */
    private boolean[][] rules;
    
    /** Create a game of the given parameters
     * 
     * @param value The start state
     * @param goal The end state
     * @param moves The number of moves to be used
     * @param rules The rules that can be used
     */
    public Game(int value, int goal, int moves, Rule[] rules) {
        this.value = value;
        this.goal = goal;
        this.movesLeft = moves;
        this.rules = Config.blankRules();
        for(Rule rule : rules) {
            this.rules[rule.getOperator()][rule.getOperand()] = true;
        }
    }
    
    public int getValue() { return value; }
    public int getGoal() { return goal; }
    public int getMovesLeft() { return movesLeft; }
    public boolean isValidRule(Rule rule) {
        return rules[rule.getOperator()][rule.getOperand()];
    }
    public State getState() {
        return new State(null, getValue(), getGoal(), getMovesLeft(), null);
    }
    
    public void makeMove(Rule rule) {
        makeMove(rule.getOperator(), rule.getOperand());
    }
    
    private void makeMove(int operator, int operand) {
        if (rules[operator][operand]) {
            switch(operator) {
                case Config.ADD:
                    value += operand;
                    break;
                case Config.SUBTRACT:
                    value -= operand;
                    break;
                default:
                    throw new RuntimeException("Unexpected operator: " + operator);
            }
            movesLeft--;
        }
    }
}
