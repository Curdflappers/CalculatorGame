package com.example.project;

public class Config {

  /** Operator index. INVALID must remain < 0 */
  public static final int INVALID = -1, ADD = 0, SUBTRACT = 1, MULTIPLY = 2,
    DIVIDE = 3, PAD = 4, SIGN = 5, DELETE = 6, CONVERT = 7, POWER = 8, REVERSE =
      9, SUM = 10, SHIFT_RIGHT = 11, SHIFT_LEFT = 12, MIRROR = 13;

  /** The string for a rule with an invalid operator */
  public static final String INVALID_STRING = "INVALID";
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
    "Reverse",
    "SUM",
    "Shift >",
    "< Shift",
    "Mirror",
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
    0, // SHIFT_RIGHT
    0, // SHIFT_LEFT
    0, // MIRROR
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

  public static final String START_PROMPT = "Enter start value: ";
  public static final String GOAL_PROMPT = "Enter goal value: ";
  public static final String MOVES_PROMPT = "Enter the number of moves: ";
  public static final String RULES_PROMPT = "Enter comma-separated rules: ";
  public static final String AGAIN_PROMPT = "Solve again (y/n): ";

  /** The user input required to quit the program */
  public static final String QUIT = "n ", CONTINUE = "y ";

  public static boolean[][] blankRules() {
    return new boolean[NUM_OPERATORS][MAX_OPERAND - MIN_OPERAND + 1];
  }

  /** The separator for rule input */
  public static final String SEPARATOR = ",";
}
