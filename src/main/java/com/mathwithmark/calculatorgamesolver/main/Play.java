package com.mathwithmark.calculatorgamesolver.main;

import java.util.Scanner;

public class Play {
    private static final String GOODBYE = "Goodbye!";
    private static final String NEXT_LEVEL = "Next level? (%c/%c): ";
    private static final String WELCOME = "Welcome to Calculator: The Game!";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println(WELCOME);
        } while (nextLevel(scanner));
        System.out.println(GOODBYE);
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
