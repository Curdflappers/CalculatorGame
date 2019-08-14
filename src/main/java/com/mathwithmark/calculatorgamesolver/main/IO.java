package com.mathwithmark.calculatorgamesolver.main;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class IO {
    private static final String INT_MESSAGE =
        "Please enter an integer between %d and %d, inclusive.";

    /**
     * @return the first character of the next line of the given scanner.
     * Returns '\0' if the next line is empty.
     * @throws NoSuchElementException if there is no next line
     */
    static char firstCharOfNextLine(Scanner scanner)
        throws NoSuchElementException {

        String input = scanner.nextLine();
        return input.length() > 0 ? input.charAt(0) : '\0';
    }

    /**
     * Enter an infinite loop asking for the input. Exit the loop once the input
     * is an integer in range.
     * @param prompt display before asking for input
     * @param min lowest acceptable value
     * @param max highest acceptable value
     * @return the number input by the user once it's in range
     */
    static int nextIntInRange(
        Scanner scanner,
        String prompt,
        int min,
        int max
    ) {
        Integer num = null;
        do {
            System.out.print(prompt);
            try {
                num = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                // swallow exception on this line to stay DRY
            }
            if (num != null && num >= min && num <= max) {
                break;
            } else {
                System.out.printf(INT_MESSAGE + "\n", min, max);
            }
        } while (true);
        return num;
    }

    /**
     * Enters an infinite loop prompting the user to enter one of the characters
     * in the set. Exits the loop when the user does so.
     * @param scanner the scanner from which to read
     * @param prompt the prompt to print out, excluding the character selection
     * @param list the set of valid characters. Must be non-empty and at least
     * one element must be possible for the user to input (i.e. not '\0'). If
     * characters are letters, they must be lower case.
     * @return the first character input that its lowercase version is an
     * element of the given set
     * @throws NoSuchElementException if the scanner runs out of lines
     */
    static char nextCharInList(
        Scanner scanner,
        String prompt,
        Character... list
    )
        throws NoSuchElementException {

        Character inputChar = null;
        List<Character> charList = Arrays.asList(list);
        String formattedPrompt = promptNextCharInList(prompt, list);
        do {
            System.out.print(formattedPrompt);
            inputChar = Character.toLowerCase(firstCharOfNextLine(scanner));
            if (charList.contains(inputChar)) break;
            else {
                printNextCharInListMessage(list);
            }
        } while (true);
        return inputChar;
    }

    private static String promptNextCharInList(
        String prompt,
        Character[] list
    ) {
        String str = prompt;
        str += " (";
        str = appendArray(str, list, "/");
        str += "): ";
        return str;
    }

    private static void printNextCharInListMessage(Object[] list) {
        System.out.printf(nextCharInListMessage(list.length) + "\n", list);
    }

    private static String nextCharInListMessage(int numChars) {
        String[] set = new String[numChars];
        for (int i = 0; i < set.length; i++) {
            set[i] = "%c";
        }
        String str = "Please enter one of '";
        str = appendArray(str, set, "', '");
        str += "' (case-insensitive).";
        return str;
    }

    private static String appendArray(
        String prompt,
        Object[] array,
        String separator
    ) {
        String str = prompt;
        for (int i = 0; i < array.length; i++) {
            str += array[i];
            if (i != array.length - 1) str += separator;
        }
        return str;
    }
}
