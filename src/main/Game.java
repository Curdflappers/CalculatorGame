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
            this.rules[rule.getOperator()][rule.getOperand()
                - Config.MIN_OPERAND] = true;
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
        return rules[rule.getOperator()][rule.getOperand()
            - Config.MIN_OPERAND];
    }

    public State getState() {
        return new State(null, getValue(), getGoal(), getMovesLeft(), null);
    }

    public Rule[] getRules() {
        return validRules;
    }

    public static double applyRule(Rule rule, double initialValue) {
        String valString = String.valueOf((int) initialValue);
        switch (rule.getOperator()) {
            case Config.ADD:
                return initialValue + rule.getOperand();
            case Config.SUBTRACT:
                return initialValue - rule.getOperand();
            case Config.MULTIPLY:
                return initialValue * rule.getOperand();
            case Config.DIVIDE:
                return initialValue / rule.getOperand();
            case Config.PAD:
                valString = String.valueOf((int) initialValue);
                valString += rule.getOperand();
                return tryParse(valString);
            case Config.SIGN:
                return -initialValue;
            case Config.DELETE:
                valString = valString.substring(0, valString.length() - 1);
                if (valString.length() == 0 || valString.equals("-")) {
                    return 0;
                }
                return tryParse(valString);
            case Config.CONVERT:
                String op1String = String.valueOf(rule.getOperand());
                String op2String = String.valueOf(rule.getOperand2());
                valString = valString.replace(op1String, op2String);
                return tryParse(valString);
            case Config.POWER:
                return Math.pow(initialValue, rule.getOperand());
            case Config.REVERSE:
                boolean negative = initialValue < 0;
                if (negative) {
                    valString = valString.substring(1); // shave off minus sign
                }
                valString = new StringBuilder(valString).reverse().toString();
                double newValue = tryParse(valString);
                return negative ? -newValue : newValue;
            case Config.SUM:
                int absValue = (int) Math.abs(initialValue);
                int sum = 0;
                while(absValue > 0) {
                    sum += absValue % 10;
                    absValue /= 10;
                }
                return sum;
            default:
                throw new RuntimeException(
                    "Unexpected operator: " + rule.getOperator());
        }
    }

    private static double tryParse(String valString) {
        try {
            return Double.parseDouble(valString);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new RuntimeException("Unexpected NumberFormatException");
        }
    }

    public void makeMove(Rule rule) {
        if (rules[rule.getOperator()][rule.getOperand()
            - Config.MIN_OPERAND]) {
            value = applyRule(rule, value);
            movesLeft--;
        }
    }
}
