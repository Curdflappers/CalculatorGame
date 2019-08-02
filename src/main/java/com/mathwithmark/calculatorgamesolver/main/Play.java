package com.mathwithmark.calculatorgamesolver.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.mathwithmark.calculatorgamesolver.calculatorgame.CalculatorGame;
import com.mathwithmark.calculatorgamesolver.calculatorgame.Config;
import com.mathwithmark.calculatorgamesolver.calculatorgame.Rule;
import com.mathwithmark.calculatorgamesolver.yaml.Serialize;

public class Play {
    private static final String GOODBYE = "Goodbye!";
    private static final String LEVEL = "Level %d";
    private static final String LEVEL_WON =
        "Congratulations, you beat the level!";
    private static final String NEXT_LEVEL = "Next level? (%c/%c): ";
    private static final String RULE = "Enter rule to apply: ";
    private static final String VALID_RULE = "Please enter a valid rule.";
    private static final String WELCOME = "Welcome to Calculator: The Game!";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int levelIndex = 1;

        System.out.println(WELCOME);
        do {
            boolean completed = playLevel(scanner, levelIndex);
            if (!completed) break;
            levelIndex++;
        } while (nextLevel(scanner));
        System.out.println(GOODBYE);
    }

    /**
     * @param num an integer between 0 and 999, inclusive
     * @return a 3-character String that is num padded with leading zeros
     */
    private static String threeDigits(int num) {
        String s = String.valueOf(num);
        while (s.length() < 3) {
            s = "0" + s;
        }
        return s;
    }

    /**
     * @param levelIndex
     * @return the path to the level from the given level index
     */
    private static String levelPathFrom(int levelIndex) {
        String threeDigitIndex = threeDigits(levelIndex);
        String path =
            Config.TEST_CASES_PATH
                + threeDigitIndex
                + Config.TEST_CASE_FILE_EXTENSION;
        return path;
    }

    private static List<String> ruleStrings(CalculatorGame level) {
        List<String> ruleStrings = new ArrayList<String>();
        for (Rule rule : level.getRules()) {
            ruleStrings.add(rule.toString());
        }
        return ruleStrings;
    }

    /**
     * Loads the given level and allows the user to play.
     * @param levelIndex the index of the level to load
     * @return true if the user completed the level, false otherwise
     */
    private static boolean playLevel(Scanner scanner, int levelIndex) {
        String levelPath = levelPathFrom(levelIndex);
        CalculatorGame level = Serialize.loadLevel(levelPath);
        System.out.printf(LEVEL + "\n", levelIndex);
        do {
            System.out.println(level);
            System.out.print(RULE);
            String inputRuleString = scanner.nextLine().trim();
            if (!ruleStrings(level).contains(inputRuleString)) {
                System.out.println(VALID_RULE);
                continue;
            }
            Rule rule = Rule.ruleFromString(inputRuleString);
            level = rule.apply(level);
        } while (!level.isWon());
        System.out.println(LEVEL_WON);
        return true;
    }

    /**
     * Prompts the user to play again
     * @return true if the user wants to go to the next level
     */
    private static boolean nextLevel(Scanner scanner) {
        char againChar = 'y';
        char quitChar = 'n';
        char inputChar = '\0';
        while (inputChar != againChar && inputChar != quitChar) {
            System.out.printf(NEXT_LEVEL, againChar, quitChar);
            String input = scanner.nextLine();
            if (input.length() != 1) {
                System.out.println("Please enter exactly one character");
                continue;
            }
            inputChar = Character.toLowerCase(input.charAt(0));
            if (inputChar != againChar && inputChar != quitChar) {
                System.out
                    .printf("Please enter '%c' or '%c'\n", againChar, quitChar);
                continue;
            }
        }
        return inputChar == againChar;
    }
}
