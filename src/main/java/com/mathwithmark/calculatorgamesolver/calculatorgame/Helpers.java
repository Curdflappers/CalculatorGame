package com.mathwithmark.calculatorgamesolver.calculatorgame;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Helpers {

    /**
     * Returns the number of digits in the given string.
     *
     * PRECONDITION the string matches the regex -?[0-9]*
     * @param valueString the string to evaluate
     * @return the number of digits in the string
     */
    public static int numDigits(String valueString) {
        int digits = (valueString.charAt(0) == '-') ? -1 : 0;
        digits += valueString.length();
        return digits;
    }

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
    public static int[] digits(int value) {
        value = Math.abs(value); // only interested in its digits, not its sign
        int numDigits = numDigits(String.valueOf(value));
        int[] digits = new int[numDigits];

        for (int i = numDigits - 1; i >= 0; i--) { // start at the end, go back
            digits[i] = value % 10;
            value /= 10;
        }

        return digits;
    }

    /**
     * Returns the digit of value at the specified index. Only works with
     * nonnegative numbers.
     * @param value the value to get the digit from
     * @param digitIndex the zero-based distance to the left of the ones place
     * (0 is ones place, 1 is tens place, 2 is hundreds place, etc.). Must be
     * nonnegative.
     */
    public static int getDigit(int value, int digitIndex) {
        return (int) (value / Math.pow(10, digitIndex)) % 10;
    }

    /**
     * Returns the value of only the digits to the left
     * Example: digitsToTheLeft(12345, 2) --> 12
     * Only works with positive values
     * @param value the value to take digits from
     * @param digitIndex the zero-based distance to the left of the ones place
     * (0 is ones place, 1 is tens place, 2 is hundreds place, etc.). Must be
     * nonnegative.
     */
    public static int digitsToTheLeft(int value, int digitIndex) {
        return value / (int) Math.pow(10, digitIndex + 1);
    }

    /** Returns the paths to every file in Config.TEST_CASES_PATH */
    static List<String> testCasePaths() {
        final File DIRECTORY = new File(Config.TEST_CASES_PATH);
        List<String> testCasePaths = new ArrayList<>();
        for (final File FILE_ENTRY : DIRECTORY.listFiles()) {
            testCasePaths.add(FILE_ENTRY.getAbsolutePath());
        }
        return testCasePaths;
    }

    /**
     * Creates one string given the array of strings, separated by the given
     * separator
     */
    public static String combineStrings(String[] strings, String separator) {
        String combinedString = "";
        int endIndex = -1;
        for (String string : strings) {
            combinedString += string + separator;
        }
        endIndex = combinedString.length() - separator.length();
        combinedString = combinedString.substring(0, endIndex);
        return combinedString;
    }

    /** Creates an array of rules from the given rule strings */
    public static Rule[] rules(String[] ruleStrings) {
        Rule[] rules = new Rule[ruleStrings.length];
        for (int i = 0; i < ruleStrings.length; i++) {
            rules[i] = Rule.ruleFromString(ruleStrings[i]);
        }
        return rules;
    }

    /** Creates an array of strings from the given rules */
    public static String[] ruleStrings(Rule[] rules) {
        String[] ruleStrings = new String[rules.length];
        for (int i = 0; i < rules.length; i++) {
            ruleStrings[i] = rules[i].toString();
        }
        return ruleStrings;
    }
}
