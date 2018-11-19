package com.example.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

public class AllTests {
  @Test
  void testRuleConstructor() {
    int operand = 1;
    int operand2 = 2;
    String ruleString = null;

    // Test all operator strings
    for (int i = 0; i < Config.OPERATOR_STRINGS.length; i++) {
      switch (Config.NUM_OPERANDS[i]) {
        case 0:
          ruleString = Config.ruleString(i);
          assertStringCreatesRule(ruleString, i);
          break;
        case 1:
          ruleString = Config.ruleString(i, operand);
          assertStringCreatesRule(ruleString, i, operand);
          break;
        case 2:
          ruleString = Config.ruleString(i, operand, operand2);
          assertStringCreatesRule(ruleString, i, operand, operand2);
          break;
      }
    }

    // "*-2" used to parse as "*-", leading to invalid operator
    operand = -2; // or any negative number
    // note the lack of space between operator and operand
    ruleString = Config.ruleString(Config.MULTIPLY, operand);
    assertStringCreatesRule(ruleString, Config.MULTIPLY, operand);
  }

  /**
   * Asserts that the given string creates a rule with the given operator and
   * that both operands are 0
   */
  void assertStringCreatesRule(String str, int operator) {
    assertStringCreatesRule(str, operator, 0, 0);
  }

  /**
   * Asserts that the given string creates a rule with the given operator and
   * that both operands are 0
   */
  void assertStringCreatesRule(String str, int operator, int operand) {
    assertStringCreatesRule(str, operator, operand, 0);
  }

  void assertStringCreatesRule(
    String str,
    int operator,
    int operand1,
    int operand2
  ) {
    Rule rule = new Rule(str);
    assertEquals(operator, rule.getOperator(), str + " operator");
    assertEquals(operand1, rule.getOperand(), str + " operand1");
    assertEquals(operand2, rule.getOperand2(), str + " operand2");
  }

  @Test
  void testOperand() {
    assertThrows(RuntimeException.class, () -> {
      addOperand(Config.MIN_OPERAND - 1);
    }, "too low operand is invalid");

    assertEquals(
      addOperand(Config.MIN_OPERAND).getOperand(),
      Config.MIN_OPERAND,
      "lower bound operand is valid"
    );

    assertEquals(
      addOperand(Config.MAX_OPERAND).getOperand(),
      Config.MAX_OPERAND,
      "upper bound operand is valid"
    );

    assertThrows(RuntimeException.class, () -> {
      addOperand(Config.MAX_OPERAND + 1);
    }, "too high operand is invalid");
  }

  /**
   * Tries to create an ADD rule with the given operand
   */
  Rule addOperand(int operand) {
    return new Rule(Config.ADD, operand);
  }

  @Test
  void testStateConstructors() {
    Rule rule = new Rule(Config.SIGN);
    int value = 1, goal = 2, movesLeft = 3;
    State parentState = new State(rule, value, goal, movesLeft, null);
    assertEquals(rule, parentState.getRule());
    assertEquals(value, parentState.getValue());
    assertEquals(goal, parentState.getGoal());
    assertEquals(movesLeft, parentState.getMovesLeft());
    assertEquals(null, parentState.getParent());

    State childState = new State(parentState, rule);
    assertEquals(rule, childState.getRule());
    assertEquals(rule.apply(parentState.getValue()), childState.getValue());
    assertEquals(parentState.getGoal(), childState.getGoal());
    assertEquals(parentState.getMovesLeft() - 1, childState.getMovesLeft());
    assertEquals(parentState, childState.getParent());
  }

  @Test
  void testGameConstructor() {
    Rule[] validRules = {
      new Rule(Config.ADD, 1),
      new Rule(Config.SIGN),
      new Rule(Config.MULTIPLY, 2),
    };
    Rule[] invalidRules = {
      new Rule(Config.ADD, 2),
      new Rule(Config.REVERSE),
      new Rule(Config.MULTIPLY, -2),
    };
    int value = 1, goal = 2, movesLeft = 3;
    Game game = new Game(value, goal, movesLeft, validRules);
    assertEquals(value, game.getValue());
    assertEquals(goal, game.getGoal());
    assertEquals(movesLeft, game.getMovesLeft());
    for (Rule rule : validRules) {
      assertTrue(game.isValidRule(rule));
    }
    for (Rule rule : invalidRules) {
      assertFalse(game.isValidRule(rule));
    }
  }

  @Test
  void testApplyRule() {
    assertApplyRule(3, Config.ADD, 2, 1);
    assertApplyRule(-3, Config.ADD, 4, -7);

    assertApplyRule(3, Config.SUBTRACT, 4, 7);
    assertApplyRule(-3, Config.SUBTRACT, 5, 2);

    assertApplyRule(12, Config.MULTIPLY, 3, 4);
    assertApplyRule(-27, Config.MULTIPLY, -9, 3);

    assertApplyRule(353, Config.DIVIDE, 2, 706);
    assertApplyRule(-535, Config.DIVIDE, -3, 535 * 3);

    assertApplyRule(12, Config.PAD, 2, 1);
    assertApplyRule(1210, Config.PAD, 10, 12);
    assertApplyRule(3, Config.PAD, 3, 0);
    assertApplyRule(30, Config.PAD, 0, 3);

    assertApplyRule(-1, Config.SIGN, 1);
    assertApplyRule(1, Config.SIGN, -1);
    assertApplyRule(-0, Config.SIGN, 0);

    assertApplyRule(1, Config.DELETE, 12);
    assertApplyRule(0, Config.DELETE, 1);
    assertApplyRule(0, Config.DELETE, 0);
    assertApplyRule(-1, Config.DELETE, -12);
    assertApplyRule(0, Config.DELETE, -1);
    assertApplyRule(0, Config.DELETE, 0);

    assertApplyRule(3, Config.CONVERT, 5, 3, 5);
    assertApplyRule(12, Config.CONVERT, 3, 1, 32);
    assertApplyRule(236523, Config.CONVERT, 56, 23, 566556);
    assertApplyRule(1, Config.CONVERT, 2, 3, 1);
    assertApplyRule(-3, Config.CONVERT, 5, 3, -5);
    assertApplyRule(-12, Config.CONVERT, 3, 1, -32);
    assertApplyRule(-236523, Config.CONVERT, 56, 23, -566556);
    assertApplyRule(-1, Config.CONVERT, 2, 3, -1);

    assertApplyRule(8, Config.POWER, 3, 2);

    assertApplyRule(35, Config.REVERSE, 53);
    assertApplyRule(0, Config.REVERSE, 0);
    assertApplyRule(-12, Config.REVERSE, -21);

    assertApplyRule(1 + 2 + 3, Config.SUM, 123);
    assertApplyRule(0, Config.SUM, 0);
  }

  void assertApplyRule(int expected, int operator, int value) {
    Rule rule = new Rule(operator);
    assertEquals(expected, rule.apply(value), 0.01);
  }

  void assertApplyRule(int expected, int operator, int operand, int value) {
    Rule rule = new Rule(operator, operand);
    assertEquals(expected, rule.apply(value), 0.01);
  }

  void assertApplyRule(
    int expected,
    int operator,
    int operand,
    int operand2,
    int value
  ) {

    Rule rule = new Rule(operator, operand, operand2);
    assertEquals(expected, rule.apply(value), 0.01);
  }

  @Test
  void testParseInput() {
    int value = 1, goal = 2, movesLeft = 3;
    String[] ruleStrings = {
      Config.ruleString(Config.ADD, 1),
      Config.ruleString(Config.SUBTRACT, 2),
      Config.ruleString(Config.MULTIPLY, -3),
    };
    String rulesString = combineStrings(ruleStrings);
    Rule[] rules = rules(ruleStrings);
    String inputString =
      value + "\n" + goal + "\n" + movesLeft + "\n" + rulesString + "\n";
    Main.parseInput(new Scanner(inputString));
    assertTrue(Main.getValue() == value);
    assertTrue(Main.getGoal() == goal);
    assertTrue(Main.getMoves() == movesLeft);
    Rule[] parsedRules = Main.getRules();
    List<Rule> parsedRulesList = Arrays.asList(parsedRules);
    for (Rule rule : rules) {
      assertTrue(
        parsedRulesList.contains(rule),
        parsedRulesList + " contains " + rule.toString()
      );
    }
  }

  /**
   * Tests integration within Main by ensuring the game is set up correctly
   */
  @Test
  void testMainGame() {
    int value = 1, goal = 2, moves = 3;
    String[] ruleStrings = {
      Config.ruleString(Config.ADD, 1),
      Config.ruleString(Config.SUBTRACT, 2),
      Config.ruleString(Config.MULTIPLY, 3),
    };
    assertCreatesGame(value, goal, moves, ruleStrings);
  }

  @Test
  void testMainSolve() {
    Rule[] rules, solution;

    // Level 1: Go from 1 to 3 using "+1" twice
    final Rule add1 = new Rule(Config.ADD, 1);
    rules = new Rule[] {
      add1, new Rule(Config.ADD, 3)
    };
    solution = new Rule[] {
      add1, add1
    };
    assertFindsSolution(0, 2, 2, rules, solution);

    // Level 4: 3 to 4 using *4, +4, /4 in three moves (in that order)
    final Rule times4 = new Rule(Config.MULTIPLY, 4);
    final Rule add4 = new Rule(Config.ADD, 4);
    final Rule div4 = new Rule(Config.DIVIDE, 4);
    rules = new Rule[] {
      times4, add4, div4
    };
    solution = new Rule[] {
      times4, add4, div4
    };
    assertFindsSolution(3, 4, 3, rules, solution);

    // Padding test: Go from 3 to 34 using pad4
    final Rule pad4 = new Rule(Config.PAD, 4);
    rules = new Rule[] {
      pad4
    };
    solution = new Rule[] {
      pad4
    };
    assertFindsSolution(3, 34, 1, rules, solution);

    // Delete test: go from 4321 to 4 using delete three times
    final Rule delete = new Rule(Config.DELETE);
    rules = new Rule[] {
      delete
    };
    solution = new Rule[] {
      delete, delete, delete
    };
    assertFindsSolution(4321, 4, 3, rules, solution);

    // Convert test: 0 to 222 using 1 and 1=>2
    final Rule pad1 = new Rule(Config.PAD, 1);
    final Rule conv1to2 = new Rule(Config.CONVERT, 1, 2);
    rules = new Rule[] {
      pad1, conv1to2
    };
    solution = new Rule[] {
      pad1, pad1, pad1, conv1to2
    };
    assertFindsSolution(0, 222, 4, rules, solution);
  }

  /**
   * Tests whether the "again" functionality works
   */
  @Test
  void testMainAgain() {
    int value = 1, goal = 2, moves = 1;
    Rule rule = new Rule(Config.ADD, 1);
    Rule[] rules = {
      rule
    };
    Rule[] solution = new Rule[] {
      rule
    };
    assertMainAgainWorks(value, goal, moves, rules, solution);
  }

  /////////////
  // Helpers //
  /////////////

  /** Asserts that the given parameters create the given game in Main.main */
  void assertCreatesGame(int value, int goal, int moves, String[] ruleStrings) {
    String inputString = inputString(false, value, goal, moves, ruleStrings);
    InputStream in = inStream(inputString);
    InputStream consoleIn = System.in;
    Rule[] inputRules = rules(ruleStrings);
    Game expectedGame = new Game(value, goal, moves, inputRules);

    // Creates game as user input
    Scanner scanner = new Scanner(in);
    Main.getInput(new String[0], scanner); // with no vm args
    assertEquals(expectedGame, Main.getGame());

    // Creates game as VM args
    System.setIn(inStream(Config.QUIT));
    Main.main(inputStrings(value, goal, moves, ruleStrings));
    assertEquals(expectedGame, Main.getGame());

    System.setIn(consoleIn);
  }

  void assertFindsSolution(
    int initialValue,
    int goal,
    int moves,
    Rule[] rules,
    Rule[] solution) {

    String[] ruleStrings = ruleStrings(rules);
    PrintStream out = System.out;
    ByteArrayOutputStream baos = prepareEndToEndTest(Config.QUIT);
    Main.main(inputStrings(initialValue, goal, moves, ruleStrings));
    String expectedOutput = solutionOutput(solution);
    String actualOutput = stringFromBaos(baos);
    assertEquals(expectedOutput, actualOutput);
    System.setOut(out);
  }

  void assertMainAgainWorks(
    int initialValue,
    int goal,
    int moves,
    Rule[] rules,
    Rule[] solution) {

    String[] ruleStrings = ruleStrings(rules);
    PrintStream out = System.out;
    String inputString =
      inputString(true, initialValue, goal, moves, ruleStrings);
    ByteArrayOutputStream baos = prepareEndToEndTest(inputString);
    Main.main(inputStrings(initialValue, goal, moves, ruleStrings));
    String expectedOutput = solutionOutput(solution);
    expectedOutput += gamePrompts();
    expectedOutput += solutionOutput(solution);
    String actualOutput = stringFromBaos(baos);
    assertEquals(expectedOutput, actualOutput);
    System.setOut(out);
  }

  /** Combines the given strings, separated with Config.SEPARATOR */
  String combineStrings(String[] strings) {
    return combineStrings(strings, Config.SEPARATOR);
  }

  /**
   * Creates one string given the array of strings, separated by the given
   * separator
   */
  String combineStrings(String[] strings, String separator) {
    String combinedString = "";
    int endIndex = -1;
    for (String string : strings) {
      combinedString += string + separator;
    }
    endIndex = combinedString.length() - separator.length();
    combinedString = combinedString.substring(0, endIndex);
    return combinedString;
  }

  String gamePrompts() {
    return Config.START_PROMPT
      + Config.GOAL_PROMPT
      + Config.MOVES_PROMPT
      + Config.RULES_PROMPT;
  }

  /**
   * Creates a newline-separated input string from the given parameters
   *
   * @param repeated whether this is for a repeat run-through. If true, prefixes
   * string with Config.CONTINUE
   */
  String inputString(
    boolean repeated,
    int value,
    int goal,
    int moves,
    String[] ruleStrings) {

    String input = repeated ? Config.CONTINUE : "";
    return input
      + value
      + "\n"
      + goal
      + "\n"
      + moves
      + "\n"
      + combineStrings(ruleStrings) + "\n"
      + Config.QUIT;
  }

  /** Simulates VM args */
  String[] inputStrings(int value, int goal, int moves, String[] ruleStrings) {
    return new String[] {
      "" + value, "" + goal, "" + moves, combineStrings(ruleStrings),
    };
  }

  /** Creates an input stream from the given input string */
  InputStream inStream(String inputString) {
    return new ByteArrayInputStream(inputString.getBytes());
  }

  /**
   * Sets output to print to BAOS, sets input to {@code input}
   *
   * @param input the new input String
   * @return the BAOS to which output is printed
   */
  private ByteArrayOutputStream prepareEndToEndTest(String input) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    System.setIn(inStream(input));
    System.setOut(ps);
    return baos;
  }

  /** Creates an array of rules from the given rule strings */
  Rule[] rules(String[] ruleStrings) {
    Rule[] rules = new Rule[ruleStrings.length];
    for (int i = 0; i < ruleStrings.length; i++) {
      rules[i] = new Rule(ruleStrings[i]);
    }
    return rules;
  }

  String[] ruleStrings(Rule[] rules) {
    String[] ruleStrings = new String[rules.length];
    for (int i = 0; i < rules.length; i++) {
      ruleStrings[i] = rules[i].toString();
    }
    return ruleStrings;
  }

  /**
   * Returns the output for the given solution
   * Includes again prompt
   */
  String solutionOutput(Rule[] solution) {
    String output = "";
    String lineEnd = "\n"; // Toggle this for Windows/non-Windows machines
    for (Rule rule : solution) {
      output += rule.toString() + lineEnd;
    }
    output += Config.AGAIN_PROMPT;
    return output;
  }

  String stringFromBaos(ByteArrayOutputStream baos) {
    return new String(baos.toByteArray(), StandardCharsets.UTF_8);
  }
}
