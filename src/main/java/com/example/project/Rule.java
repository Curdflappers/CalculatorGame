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
            if (synonym.equalsIgnoreCase(Config.OPERATOR_STRINGS[i])) {
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
            System.out
                .println("Unexpected NumberFormatException in Rule.setOperand");
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
                .println(
                    "Unexpected NumberFormatException in Rule.setOperand2"
                );
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
        int numOperands = Config.NUM_OPERANDS[operator];
        switch (numOperands) {
            case 0:
                string = Config.ruleString(operator);
                return;
            case 1:
                string = Config.ruleString(operator, operand);
                return;
            case 2:
                string = Config.ruleString(operator, operand, operand2);
                return;
            default:
                throw new RuntimeException(
                    "Unexpected number of operands: " + numOperands
                );
        }
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

    /**
     * Returns the result of applying this rule to the given value
     */
    public double apply(double value) {
        String valString = String.valueOf((int) value);
        int op = getOperand();
        int op2 = getOperand2();
        boolean negative = value < 0;
        int[] digits;
        switch (getOperator()) {
            case Config.ADD:
                return value + op;
            case Config.SUBTRACT:
                return value - op;
            case Config.MULTIPLY:
                return value * op;
            case Config.DIVIDE:
                return value / op;
            case Config.PAD:
                valString = String.valueOf((int) value);
                valString += op;
                return Double.parseDouble(valString);
            case Config.SIGN:
                return -value;
            case Config.DELETE:
                valString = valString.substring(0, valString.length() - 1);
                if (valString.length() == 0 || valString.equals("-")) {
                    return 0;
                }
                return Double.parseDouble(valString);
            case Config.CONVERT:
                String op1String = String.valueOf(op);
                String op2String = String.valueOf(op2);
                valString = valString.replace(op1String, op2String);
                return Double.parseDouble(valString);
            case Config.POWER:
                return Math.pow(value, op);
            case Config.REVERSE:
                if (negative) {
                    valString = valString.substring(1); // shave off minus sign
                }
                valString = new StringBuilder(valString).reverse().toString();
                double newValue = Double.parseDouble(valString);
                return negative ? -newValue : newValue;
            case Config.SUM:
                int absValue = (int) value;
                int sum = 0;
                while (absValue != 0) {
                    sum += absValue % 10;
                    absValue /= 10;
                }
                return sum;
            case Config.SHIFT_RIGHT:
                digits = digits((int) value);
                rotateRight(digits);
                newValue = valueOf(digits);
                return negative ? -newValue : newValue;
            case Config.SHIFT_LEFT:
                digits = digits((int) value);
                rotateLeft(digits);
                newValue = valueOf(digits);
                return negative ? -newValue : newValue;
            case Config.MIRROR:
                if (negative) {
                    valString = valString.substring(1); // shave off minus sign
                }
                // add reversed string to end of current string
                valString += new StringBuilder(valString).reverse().toString();
                newValue = Double.parseDouble(valString);
                return negative ? -newValue : newValue;

            default: // should never get here
                throw new RuntimeException(
                    "Unexpected error when applying rule:\n"
                        + this
                        + "\n"
                        + value
                );
        }
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

    /////////////
    // HELPERS //
    /////////////

    /**
     * Returns an array of digits for this value
     * <p>
     * <code>digits(1234) returns [1, 2, 3, 4]</code>
     * <p>
     * <code>digits(-2) returns [2]</code>
     * <p>
     * <code>digits(0) returns [0]</code>
     *
     * @param value any integer
     * @return an array representation of the absolute value of value
     */
    private int[] digits(int value) {
        value = Math.abs(value); // only interested in its digits, not its sign
        int numDigits = (int) Math.ceil(Math.log10(value));
        int[] digits = new int[numDigits];

        for (int i = numDigits - 1; i >= 0; i--) { // start at the end, go back
            digits[i] = value % 10;
            value /= 10;
        }

        return digits;
    }

    /**
     * Rotates the given array right once
     * <p>
     * <code>rotateRight([1, 2, 3, 4])</code> changes the argument to
     * <code>[4, 1, 2, 3]</code>
     *
     * @param digits an array of integers
     */
    private void rotateRight(int[] digits) {
        int last = digits[digits.length - 1];
        for (int i = digits.length - 1; i > 0; i--) {
            digits[i] = digits[i - 1];
        }
        digits[0] = last; // and the last shall be first
    }

    /**
     * Rotates the given array right once
     * <p>
     * <code>rotateLeft([1, 2, 3, 4])</code> changes the argument to
     * <code>[2, 3, 4, 1]</code>
     *
     * @param digits an array of integers
     */
    private void rotateLeft(int[] digits) {
        int first = digits[0];
        for (int i = 0; i < digits.length - 1; i++) {
            digits[i] = digits[i + 1];
        }
        digits[digits.length - 1] = first; // and the first shall be last
    }

    /**
     * Returns the value represented by the given array
     * <p>
     * <code>valueOf([1, 2, 3, 4]) returns 1234</code>
     * <p>
     * <code>valueOf([0]) returns 0</code>
     * <p>
     * <code>valueOf([]) returns 0</code>
     * @param digits the array to evaluate
     */
    private int valueOf(int[] digits) {
        int value = 0;

        for (int i = 0; i < digits.length; i++) {
            value *= 10;
            value += digits[i];
        }

        return value;
    }
}
