package com.mathwithmark.calculatorgamesolver.calculatorgame;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Helpers {
    /**
     * @return the number of digits in the given integer
     */
    public static int numDigits(int value) {
        if (value == 0) return 1;
        return (int) Math.floor(Math.log10(Math.abs(value))) + 1;
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
    public static List<String> testCasePaths() {
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
            rules[i] = Rule.of(ruleStrings[i]);
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
