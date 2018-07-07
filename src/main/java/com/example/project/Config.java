package com.example.project;

public class Config {
    public static final int ADD = 0, SUBTRACT = 1, MULTIPLY = 2, DIVIDE = 3,
        PAD = 4, SIGN = 5, DELETE = 6, CONVERT = 7, POWER = 8, REVERSE = 9,
        SUM = 10;

    public static final int NUM_OPERATORS = 11;
    public static final int MIN_OPERAND = -999, MAX_OPERAND = 999;

    public static final String START_PROMPT = "Enter start value: ",
        GOAL_PROMPT = "Enter goal value: ",
        MOVES_PROMPT = "Enter the number of moves: ",
        RULES_PROMPT = "Enter comma-separated rules: ",
        AGAIN_PROMPT = "Solve again (y/n): ";

    public static boolean[][] blankRules() {
        return new boolean[NUM_OPERATORS][MAX_OPERAND - MIN_OPERAND + 1];
    }
}
