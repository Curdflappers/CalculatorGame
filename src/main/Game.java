package main;

public class Game {
    /** The current number for this game */
    private int state;
    
    /** The goal number for this game */
    private int goal;
    
    /** The moves left in this game */
    private int movesLeft;
    
    /** The rules that can be used in this game */
    private boolean[][] rules;
    
    /** Create a game of the given parameters
     * 
     * @param state The start state
     * @param goal The end state
     * @param moves The number of moves to be used
     * @param rules The rules that can be used
     */
    public Game(int state, int goal, int moves, Rule[] rules) {
        this.state = state;
        this.goal = goal;
        this.movesLeft = moves;
        this.rules = Config.blankRules();
        for(Rule rule : rules) {
            this.rules[rule.getOperator()][rule.getOperand()] = true;
        }
    }
    
    public int getState() { return state; }
    public int getGoal() { return goal; }
    public int getMovesLeft() { return movesLeft; }
    public boolean isValidRule(Rule rule) {
        return rules[rule.getOperator()][rule.getOperand()];
    }
    
    public void makeMove(int operator, int operand) {
        if (0 <= operator && operator < Config.NUM_OPERATORS
                && 0 < operand && operand <= Config.NUM_OPERANDS
                && rules[operator][operand]) {
            switch(operator) {
                case Config.ADD:
                    state += operand;
                    break;
                case Config.SUBTRACT:
                    state -= operand;
                    break;
                default:
                    throw new RuntimeException("Unexpected operator: " + operator);
            }
            movesLeft--;
        }
    }
}
