package com.mathwithmark.calculatorgamesolver.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.mathwithmark.calculatorgamesolver.calculatorgame.CalculatorGame;
import com.mathwithmark.calculatorgamesolver.calculatorgame.Config;
import com.mathwithmark.calculatorgamesolver.calculatorgame.Rule;
import com.mathwithmark.calculatorgamesolver.yaml.Serialize;

public class Play {
    static final int NUM_LEVELS = 199;

    // None of these final Strings end with newline characters

    // Special inputs to track
    static final char YES_INPUT = 'y';
    static final char NO_INPUT = 'n';
    static final String QUIT_LEVEL_INPUT = "quit";
    static final String RESTART_LEVEL_INPUT = "clear";

    // Messages do not prompt for user input
    static final String GAME_WON_MESSAGE =
        "Congratulations, you beat the game!";
    static final String GOODBYE_MESSAGE = "Goodbye!";
    static final String HIGHEST_LEVEL_MESSAGE = "You got to level %d.";
    private static final String INT_MESSAGE =
        "Please enter an integer between %d and %d, inclusive";
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
    private static final String FIRST_LEVEL_PROMPT =
        String
            .format(
                "Enter the first level: (%d to %d, inclusive): ",
                1,
                NUM_LEVELS
            );
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

        System.out.println(WELCOME_MESSAGE);
        int levelIndex = getFirstLevel(scanner);
        do {
            boolean completed = playLevel(scanner, levelIndex);
            if (!completed) break;
            levelIndex++;
        } while (levelIndex <= NUM_LEVELS && nextLevel(scanner));

        if (levelIndex > NUM_LEVELS) {
            System.out.println(GAME_WON_MESSAGE);
        } else {
            System.out.printf(HIGHEST_LEVEL_MESSAGE + "\n", levelIndex);
        }
        System.out.println(GOODBYE_MESSAGE);
    }

    /**
     * @param prompt display before asking for input
     * @param min lowest acceptable value
     * @param max highest acceptable value
     * @return the number input by the user once it's in range
     */
    private static int getNumberInRange(
        Scanner scanner,
        String prompt,
        int min,
        int max
    ) {
        Integer number = null;
        do {
            System.out.print(prompt);
            try {
                number = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                // swallow exception for now, print message later to stay DRY
            }
            if(number != null && number >= min && number <= max) {
                break;
            } else {
                System.out.printf(INT_MESSAGE + "\n", min, max);
            }
        } while (true);
        return number;
    }

    /**
     * Return the 1-based index of the first level the play wants to play
     */
    private static int getFirstLevel(Scanner scanner) {
        return getNumberInRange(scanner, FIRST_LEVEL_PROMPT, 1, NUM_LEVELS);
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
        CalculatorGame originalLevel = Serialize.loadTestCase(levelPath).GAME;
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
            Rule rule =
                Arrays
                    .asList(level.getRules())
                    .stream()
                    .filter(r -> r.toString().equals(input))
                    .findFirst()
                    .get();
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
        char inputChar = '\0';
        while (inputChar != YES_INPUT && inputChar != NO_INPUT) {
            System.out.printf(NEXT_LEVEL_PROMPT, YES_INPUT, NO_INPUT);
            String input = scanner.nextLine();
            if (input.length() != 1) {
                System.out.println("Please enter exactly one character");
                continue;
            }
            inputChar = Character.toLowerCase(input.charAt(0));
            if (inputChar != YES_INPUT && inputChar != NO_INPUT) {
                System.out
                    .printf("Please enter '%c' or '%c'\n", YES_INPUT, NO_INPUT);
                continue;
            }
        }
        return inputChar == YES_INPUT;
    }
}
