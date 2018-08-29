package com.example.project;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Rule {
  private int operand;
  private int operand2; // for A=>B rules
  /** The index associated with the operator */
  private int operator;
  private String string;

  public Rule(String rule) {
    Matcher convertMatcher = Pattern.compile("\\d+=>\\d+").matcher(rule);
    boolean isConvertRule = convertMatcher.find();
    if (isConvertRule) {
      int arrowIndex = rule.indexOf("=>");
      setOperand(rule.substring(0, arrowIndex));
      setOperand2(rule.substring(arrowIndex + 2));
      setOperator(Config.CONVERT);
      setString();
      return;
    }

    Matcher matcher = Pattern.compile("-?\\d+").matcher(rule);
    boolean hasInt = matcher.find();

    // Don't accidentally pad a negative
    String operator = hasInt ? rule.substring(0, matcher.start()) : rule;
    if (operator.equals("") && rule.charAt(0) == '-') {
      setOperator(toOperator("-")); // the minus was for subtraction
      setOperand(rule.substring(1)); // skip the minus sign in the operand
      setString();
      return;
    }
    setOperator(toOperator(operator));
    if (hasInt) {
      setOperand(matcher.group());
    }
    setString();
  }

  public Rule(int operator) {
    setOperator(operator);
    setString();
  }

  public Rule(int operator, int operand) {
    setOperator(operator);
    setOperand(operand);
    setString();
  }

  public Rule(int operator, int operand, int operand2) {
    setOperator(operator);
    setOperand(operand);
    setOperand2(operand2);
    setString();
  }

  /**
   * Transform given synonym into the method name of the corresponding
   * operator
   * 
   * @param synonym a potential synonym for an operator method name
   * @return the method name if the synonym is recognized
   * @throws RuntimeException when synonym is not recognized
   */
  private static int toOperator(String synonym) {
    synonym = synonym.trim().toLowerCase();
    for (int i = 0; i < Config.OPERATOR_STRINGS.length; i++) {
      if (synonym.equals(Config.OPERATOR_STRINGS[i])) {
        return i;
      }
    }
    throw new RuntimeException("Invalid operator: " + synonym);
  }

  private void setOperator(int operatorIndex) {
    this.operator = operatorIndex;
  }

  private void setOperand(String operand) {
    try {
      setOperand(Integer.parseInt(operand));
    } catch (NumberFormatException e) {
      System.out.println("Unexpected NumberFormatException in Rule.setOperand");
      e.printStackTrace();
    }
  }

  private void setOperand(int operand) {
    if (operand > Config.MAX_OPERAND || operand < Config.MIN_OPERAND) {
      throw new RuntimeException("Operand out of range: " + operand);
    } else
      this.operand = operand;
  }

  private void setOperand2(String operand2) {
    try {
      setOperand2(Integer.parseInt(operand2));
    } catch (NumberFormatException e) {
      System.out
        .println("Unexpected NumberFormatException in Rule.setOperand2");
      e.printStackTrace();
    }
  }

  private void setOperand2(int operand2) {
    if (operand2 > Config.MAX_OPERAND || operand2 < Config.MIN_OPERAND) {
      throw new RuntimeException("Operand out of range: " + operand2);
    } else
      this.operand2 = operand2;
  }

  public int getOperator() {
    return operator;
  }

  public int getOperand() {
    return operand;
  }

  public int getOperand2() {
    return operand2;
  }

  private void setString() {
    String s = "";
    switch (operator) {
      case Config.ADD:
        s += "+";
        break;
      case Config.SUBTRACT:
        s += "-";
        break;
      case Config.MULTIPLY:
        s += "*";
        break;
      case Config.DIVIDE:
        s += "/";
        break;
      case Config.SIGN:
        string = "+/-";
        return;
      case Config.DELETE:
        string = "<<";
        return;
      case Config.CONVERT:
        String op1String = String.valueOf(getOperand());
        String op2String = String.valueOf(getOperand2());
        string = op1String + "=>" + op2String;
        return;
      case Config.POWER:
        s += "x^";
        break;
      case Config.REVERSE:
        string = "Reverse";
        return;
      case Config.SUM:
        string = "SUM";
        return;
    }
    s += operand;
    string = s;
  }

  /**
   * Returns a string representation of this rule.
   * <p>
   * In the form [operator][operand] (no spaces) e.g. "+1", "*2"
   * <p>
   * Operators are: ADD: "+", SUBTRACT: "-", MULTIPLY: "*", DIVIDE: "/"
   */
  public String toString() {
    return string;
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof Rule) {
      Rule otherRule = (Rule) other;
      return otherRule.getOperator() == getOperator()
        && otherRule.getOperand() == getOperand()
        && otherRule.getOperand2() == getOperand2();
    }
    return false;
  }
}
