package com.example.project;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Rule {
    private int operand;
    private int operand2; // for convert op1 to op2 rules
    /** The index associated with the operator */
    private int operator;
    private String string;

    public static Rule ruleFromString(String ruleString) {
        int operator, operand1 = 0, operand2;
        String convertString = Config.OPERATOR_STRINGS[Config.CONVERT];
        Matcher convertMatcher =
            Pattern
                .compile("\\d+" + convertString + "\\d+")
                .matcher(ruleString);
        boolean isConvertRule = convertMatcher.find();
        if (isConvertRule) {
            int arrowIndex = ruleString.indexOf(convertString);
            operator = Config.CONVERT;
            operand1 = Integer.parseInt(ruleString.substring(0, arrowIndex));
            operand2 = Integer.parseInt(ruleString.substring(arrowIndex + 2));
            return new ConvertRule(operator, operand1, operand2);
        }

        Matcher matcher = Pattern.compile("-?\\d+").matcher(ruleString);
        boolean hasInt = matcher.find();

        // Don't accidentally pad a negative
        String operatorString =
            hasInt ? ruleString.substring(0, matcher.start()) : ruleString;
        if (operatorString.equals("") && ruleString.charAt(0) == '-') {
            operator = Config.SUBTRACT; // the minus was for subtraction
            // skip the minus sign in the operand
            operand1 = Integer.parseInt(ruleString.substring(1));
            return new Rule(operator, operand1);
        }

        // We have a basic rule of the form "[operator][op1?]"
        operator = toOperator(operatorString);
        if (hasInt) {
            operand1 = Integer.parseInt(matcher.group());
        }
        return new Rule(operator, operand1);
    }

    public Rule() {
        this(Config.INVALID, 0, 0);
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

    private void setOperand(int operand) {
        if (operand > Config.MAX_OPERAND || operand < Config.MIN_OPERAND) {
            throw new RuntimeException("Operand out of range: " + operand);
        } else
            this.operand = operand;
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
        if (operator < 0) {
            string = "INVALID";
            return;
        }
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

    public Game apply(Game game) {
        double newValue = apply(game.getValue());
        return new Game(
            newValue,
            game.getGoal(),
            game.getMovesLeft() - 1,
            game.getValidRules()
        );
    }

    /**
     * Returns the result of applying this rule to the given value
     */
    private double apply(double value) {
        switch (getOperator()) {
            case Config.ADD:
                return add(value);
            case Config.SUBTRACT:
                return subtract(value);
            case Config.MULTIPLY:
                return multiply(value);
            case Config.DIVIDE:
                return divide(value);
            case Config.PAD:
                return pad(value);
            case Config.SIGN:
                return sign(value);
            case Config.DELETE:
                return delete(value);
            case Config.POWER:
                return power(value);
            case Config.REVERSE:
                return reverse(value);
            case Config.SUM:
                return sum(value);
            case Config.SHIFT_RIGHT:
                return shiftRight(value);
            case Config.SHIFT_LEFT:
                return shiftLeft(value);
            case Config.MIRROR:
                return mirror(value);

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

    ///////////////////////
    // RULE APPLICATIONS //
    ///////////////////////

    private double add(double value) {
        return value + getOperand();
    }

    private double subtract(double value) {
        return value - getOperand();
    }

    private double multiply(double value) {
        return value * getOperand();
    }

    private double divide(double value) {
        return value / getOperand();
    }

    private double pad(double value) {
        String valString = String.valueOf((int) value);
        valString += getOperand();
        return Double.parseDouble(valString);
    }

    private double sign(double value) {
        return -value;
    }

    private double delete(double value) {
        String valString = String.valueOf((int) value);
        valString = valString.substring(0, valString.length() - 1);
        if (valString.length() == 0 || valString.equals("-")) {
            return 0;
        }
        return Double.parseDouble(valString);
    }

    private double power(double value) {
        return Math.pow(value, getOperand());
    }

    private double reverse(double value) {
        boolean negative = value < 0;
        String valString = String.valueOf((int) value);
        if (negative) {
            valString = valString.substring(1); // shave off minus sign
        }
        valString = new StringBuilder(valString).reverse().toString();
        double newValue = Double.parseDouble(valString);
        return negative ? -newValue : newValue;
    }

    private double sum(double value) {
        int absValue = (int) value;
        int sum = 0;
        while (absValue != 0) {
            sum += absValue % 10;
            absValue /= 10;
        }
        return sum;
    }

    private double shiftRight(double value) {
        int[] digits = digits((int) value);
        rotateRight(digits);
        double newValue = valueOf(digits);
        return value >= 0 ? newValue : -newValue;
    }

    private double shiftLeft(double value) {
        int[] digits = digits((int) value);
        rotateLeft(digits);
        double newValue = valueOf(digits);
        return value >= 0 ? newValue : -newValue;
    }

    private double mirror(double value) {
        boolean negative = value < 0;
        String valString = String.valueOf((int) value);
        if (negative) {
            valString = valString.substring(1); // shave off minus sign
        }
        // add reversed string to end of current string
        valString += new StringBuilder(valString).reverse().toString();
        double newValue = Double.parseDouble(valString);
        return negative ? -newValue : newValue;
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
