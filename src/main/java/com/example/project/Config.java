package com.example.project;

public class Config {

  public static final int ADD = 0, SUBTRACT = 1, MULTIPLY = 2, DIVIDE = 3,
    PAD = 4, SIGN = 5, DELETE = 6, CONVERT = 7, POWER = 8, REVERSE = 9,
    SUM = 10;

  public static final String[] OPERATOR_STRINGS = {
    "+",
    "-",
    "*",
    "/",
    "", // PAD
    "+/-",
    "<<",
    "=>",
    "^",
    "reverse",
    "sum"
  };

  public static final int[] NUM_OPERANDS = {
    1, // ADD
    1, // SUBTRACT
    1, // MULTIPLY
    1, // DIVIDE
    1, // PAD
    0, // SIGN
    0, // DELETE
    2, // CONVERT
    1, // POWER
    0, // REVERSE
    0, // SUM
  };

  /**
   * Returns the rule string for the given operator
   * If the operator takes operands, this throws an exception
   */
  public static String ruleString(int operator) {
    if (NUM_OPERANDS[operator] != 0) {
      throw new RuntimeException("invalid number of operands");
    }
    return Config.OPERATOR_STRINGS[operator];
  }

  /**
   * Returns the rule string for the given operator and operand
   * If the operator doesn't take exactly 1 operand, this throws an exception
   */
  public static String ruleString(int operator, int operand) {
    if (NUM_OPERANDS[operator] != 1) {
      throw new RuntimeException("invalid number of operands");
    }
    return Config.OPERATOR_STRINGS[operator] + operand;
  }

  /**
   * Returns the rule string for the given operand and operands
   * If the operator doesn't take exactly 2 operands, this throws an exception
   */
  public static String ruleString(int operator, int operand, int operand2) {
    if (NUM_OPERANDS[operator] != 2) {
      throw new RuntimeException("invalid number of operands");
    }
    return operand + Config.OPERATOR_STRINGS[operator] + operand2;
  }

  public static final int NUM_OPERATORS = OPERATOR_STRINGS.length;
  public static final int MIN_OPERAND = -999, MAX_OPERAND = 999;

  public static final String START_PROMPT = "Enter start value: ",
    GOAL_PROMPT = "Enter goal value: ",
    MOVES_PROMPT = "Enter the number of moves: ",
    RULES_PROMPT = "Enter comma-separated rules: ",
    AGAIN_PROMPT = "Solve again (y/n): ";

  /** The user input required to quit the program */
  public static final String QUIT = "n ", CONTINUE = "y ";

  public static boolean[][] blankRules() {
    return new boolean[NUM_OPERATORS][MAX_OPERAND - MIN_OPERAND + 1];
  }

  /** The separator for rule input */
  public static final String SEPARATOR = ",";
}
