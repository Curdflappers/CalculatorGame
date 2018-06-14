package main;

public class Game {
    /** The current number for this game */
    private double value;

    /** The goal number for this game */
    private int goal;

    /** The moves left in this game */
    private int movesLeft;

    /** The rules that can be used in this game */
    private boolean[][] rules;
    private Rule[] validRules;

    /**
     * Create a game of the given parameters
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
        for (Rule rule : rules) {
            this.rules[rule.getOperator()][rule.getOperand()] = true;
        }
        this.validRules = rules;
    }

    public double getValue() {
        return value;
    }

    public int getGoal() {
        return goal;
    }

    public int getMovesLeft() {
        return movesLeft;
    }

    public boolean isValidRule(Rule rule) {
        return rules[rule.getOperator()][rule.getOperand()];
    }

    public State getState() {
        return new State(null, getValue(), getGoal(), getMovesLeft(), null);
    }

    public Rule[] getRules() {
        return validRules;
    }

    public static double applyRule(Rule rule, double initialValue) {
        switch (rule.getOperator()) {
            case Config.ADD:
                return initialValue + rule.getOperand();
            case Config.SUBTRACT:
                return initialValue - rule.getOperand();
            case Config.MULTIPLY:
                return initialValue * rule.getOperand();
            case Config.DIVIDE:
                return initialValue / rule.getOperand();
            default:
                throw new RuntimeException(
                    "Unexpected operator: " + rule.getOperator());
        }
    }

    public void makeMove(Rule rule) {
        if (rules[rule.getOperator()][rule.getOperand()]) {
            value = applyRule(rule, value);
            movesLeft--;
        }
    }
}
