package com.mathwithmark.calculatorgamesolver.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.mathwithmark.calculatorgamesolver.calculatorgame.CalculatorGame;
import com.mathwithmark.calculatorgamesolver.calculatorgame.Config;
import com.mathwithmark.calculatorgamesolver.calculatorgame.Rule;
import com.mathwithmark.calculatorgamesolver.yaml.Serialize;

public class Play {
    // None of these final Strings end with newline characters

    // Special inputs to track
    static final char QUIT_GAME_INPUT = 'n';
    static final String QUIT_LEVEL_INPUT = "quit";
    static final String RESTART_LEVEL_INPUT = "clear";

    // Messages do not prompt for user input
    static final String GOODBYE_MESSAGE = "Goodbye!";
    static final String HIGHEST_LEVEL_MESSAGE = "You got to level %d.";
    static final String LEVEL_TITLE_MESSAGE = "Level %d";
    static final String LEVEL_WON_MESSAGE =
        "Congratulations, you beat the level!";
    /**
     * Message to display when the user enters invalid input for the rule prompt
     */
    static final String VALID_RULE_MESSAGE =
        String
            .format(
                "Please enter a valid rule, '%s', or '%s'.",
                QUIT_LEVEL_INPUT,
                RESTART_LEVEL_INPUT
            );
    static final String WELCOME_MESSAGE = "Welcome to Calculator: The Game!";

    // Prompts prompt for user input, and always end with ": "
    static final String NEXT_LEVEL_PROMPT = "Next level? (%c/%c): ";
    static final String RULE_PROMPT =
        String
            .format(
                "Enter rule to apply "
                    + "('%s' to quit, '%s' to restart the level): ",
                QUIT_LEVEL_INPUT,
                RESTART_LEVEL_INPUT
            );

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int levelIndex = 1;

        System.out.println(WELCOME_MESSAGE);
        do {
            boolean completed = playLevel(scanner, levelIndex);
            if (!completed) break;
            levelIndex++;
        } while (nextLevel(scanner));
        System.out.printf(HIGHEST_LEVEL_MESSAGE + "\n", levelIndex);
        System.out.println(GOODBYE_MESSAGE);
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
     * @param input the input to validate
     * @param level the level for which the input was given
     * @return whether the input is valid for the level. True if the input is a
     * rule of the level, the QUIT_INPUT, or the RESTART_LEVEL_INPUT. False
     * otherwise.
     */
    private static boolean validRuleInput(String input, CalculatorGame level) {
        return ruleStrings(level).contains(input)
            || input.equals(QUIT_LEVEL_INPUT)
            || input.equals(RESTART_LEVEL_INPUT);
    }

    /**
     * Loads the given level and allows the user to play.
     * @param levelIndex the index of the level to load
     * @return true if the user completed the level, false otherwise
     */
    private static boolean playLevel(Scanner scanner, int levelIndex) {
        String levelPath = levelPathFrom(levelIndex);
        CalculatorGame originalLevel = Serialize.loadLevel(levelPath);
        CalculatorGame level = originalLevel;
        System.out.printf(LEVEL_TITLE_MESSAGE + "\n", levelIndex);
        do {
            System.out.println(level);
            System.out.print(RULE_PROMPT);
            String input = scanner.nextLine().trim();
            boolean inputIsValid = validRuleInput(input, level);
            if (!inputIsValid) {
                System.out.println(VALID_RULE_MESSAGE);
                continue;
            }
            if (input.equals(QUIT_LEVEL_INPUT)) return false;
            if (input.equals(RESTART_LEVEL_INPUT)) {
                level = originalLevel;
                continue;
            }
            Rule rule = Rule.ruleFromString(input);
            level = rule.apply(level);
        } while (!level.isWon());
        System.out.println(LEVEL_WON_MESSAGE);
        return true;
    }

    /**
     * Prompts the user to play again
     * @return true if the user wants to go to the next level
     */
    private static boolean nextLevel(Scanner scanner) {
        char againChar = 'y';
        char inputChar = '\0';
        while (inputChar != againChar && inputChar != QUIT_GAME_INPUT) {
            System.out.printf(NEXT_LEVEL_PROMPT, againChar, QUIT_GAME_INPUT);
            String input = scanner.nextLine();
            if (input.length() != 1) {
                System.out.println("Please enter exactly one character");
                continue;
            }
            inputChar = Character.toLowerCase(input.charAt(0));
            if (inputChar != againChar && inputChar != QUIT_GAME_INPUT) {
                System.out
                    .printf("Please enter '%c' or '%c'\n", againChar, QUIT_GAME_INPUT);
                continue;
            }
        }
        return inputChar == againChar;
    }
}
