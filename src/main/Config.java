package main;

public class Config {
    public static final int 
        ADD = 0,
        SUBTRACT = 1,
        MULTIPLY = 2,
        DIVIDE = 3;
    
    public static final int NUM_OPERATORS = 4;
    public static final int NUM_OPERANDS = 10;
    
    public static boolean[][] blankRules() {
        return new boolean[NUM_OPERATORS][NUM_OPERANDS];
    }
}
