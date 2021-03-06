package com.mathwithmark.calculatorgamesolver.calculatorgame;

public class Config {
  /** The path to the directory containing all test cases */
  public static String TEST_CASES_PATH = "test-cases/";
  /** The file extension for every test case file */
  public static String TEST_CASE_FILE_EXTENSION = ".yaml";

  /** The string for a rule with an invalid operator */
  static final String INVALID_STRING = "INVALID";
  static final String[] OPERATOR_STRINGS = {
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
    "[+]",
    "Store",
    "Inv10",
    "Update Store",
  };

  static final int[] NUM_OPERANDS = {
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
    1, // META_ADD
    0, // STORE (takes zero on creation)
    0, // INVERSE_TEN
    0, // UPDATE_STORE
  };

  /** The maximum number of digits in a CalculatorGame */
  static final int MAX_DIGITS = 6;

  /**
   * Returns the rule string for the given operator
   */
  static String ruleString(int operator) {
    return Config.OPERATOR_STRINGS[operator];
  }

  /**
   * Returns the rule string for the given operator and operand
   */
  public static String ruleString(int operator, int operand1) {
    return Config.OPERATOR_STRINGS[operator] + operand1;
  }

  static final int NUM_OPERATORS = OPERATOR_STRINGS.length;

  public static final String START_PROMPT = "Enter start value: ";
  public static final String GOAL_PROMPT = "Enter goal value: ";
  public static final String MOVES_PROMPT = "Enter the number of moves: ";
  public static final String RULES_PROMPT =
    "Enter one rule per line (empty string to mark end of list):\n";
  public static final String PORTALS_PRESENT_PROMPT =
    "Are there any portals for this level? (y/n): ";
  public static final String LEFT_PORTAL_PROMPT =
    "Enter the distance from the ones place of the portal on the left: ";
  public static final String RIGHT_PORTAL_PROMPT =
    "Enter the distance from the ones place of the portal on the right: ";
  public static final String AGAIN_PROMPT = "Solve again (y/n): ";
  public static final String SOLUTION_PROMPT = "Solution:";

  /** The user input required to quit the program */
  public static final String QUIT = "n ";

  public static final String CONTINUE = "y ";

  /** The separator for rule input through command-line arguments */
  public static final String CMDLINE_SEPARATOR = ",";

  /** What to display when the level is unsolvable */
  public static final String UNSOLVABLE_PROMPT = "Level unsolvable";
}
