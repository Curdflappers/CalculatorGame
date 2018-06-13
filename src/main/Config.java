package main;

public class Config {
    public static final int ADD = 0, SUBTRACT = 1, MULTIPLY = 2, DIVIDE = 3;

    public static final int NUM_OPERATORS = 4;
    public static final int NUM_OPERANDS = 10;

    public static final String START_PROMPT = "Enter start value: ",
        GOAL_PROMPT = "Enter goal value: ",
        MOVES_PROMPT = "Enter the number of moves: ",
        RULES_PROMPT = "Enter comma-separated rules: ";

    public static boolean[][] blankRules() {
        return new boolean[NUM_OPERATORS][NUM_OPERANDS];
    }
}
