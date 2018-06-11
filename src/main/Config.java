package main;

public class Config {
    public static final int 
        ADD = 0,
        SUBTRACT = 1;
    
    public static final int NUM_OPERATORS = 2;
    public static final int NUM_OPERANDS = 10;
    
    public static boolean[][] blankRules() {
        return new boolean[NUM_OPERATORS][NUM_OPERANDS];
    }
}
