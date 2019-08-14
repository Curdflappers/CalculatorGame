package com.mathwithmark.calculatorgamesolver.main;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import com.mathwithmark.calculatorgamesolver.brutesolver.Solver;
import com.mathwithmark.calculatorgamesolver.calculatorgame.CalculatorGame;
import com.mathwithmark.calculatorgamesolver.calculatorgame.Config;
import com.mathwithmark.calculatorgamesolver.yaml.Serialize;

public class Developer {
    static final char QUIT_INPUT = 'q';

    private static final String QUIT_PROMPT =
        String
            .format(
                "Enter '%c' to quit. Enter anything else to continue: ",
                QUIT_INPUT
            );
    private static final String TEST_CASE_NAME_PROMPT =
        "Filename (\""
            + Config.TEST_CASE_FILE_EXTENSION
            + "\" will be added): ";
    private static final String SAVE_TEST_CASE_PROMPT =
        "Save test case (y/n): ";
    private static final String FILE_EXISTS_PROMPT =
        "That file already exists. "
            + "Save under [D]ifferent name? "
            + "[O]verwrite it? "
            + "[S]kip this test case?";

    private static final String GOODBYE_MESSAGE = "Exiting program. Goodbye!";

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        do {
            Main.getInput(args, scanner);
            List<List<String>> solutions =
                Solver.getAllSolutions(Main.getCalculatorGame());
            List<String> solution = solutions.get(0);
            Main.printSolutions(solutions);
            promptSaveTestCase(scanner, Main.getCalculatorGame(), solution);
        } while (!promptQuit(scanner));
        System.out.println(GOODBYE_MESSAGE);
    }

    /**
     * Prompts the user to quit
     * @return true iff the user enters the quit input
     */
    private static boolean promptQuit(Scanner scanner) {
        System.out.print(QUIT_PROMPT);
        return IO.firstCharOfNextLine(scanner) == QUIT_INPUT;
    }

    /**
     * Prompt the user to see if they want to save the test case
     * @param scanner
     * @throws IOException
     */
    private static void promptSaveTestCase(
        Scanner scanner,
        CalculatorGame game,
        List<String> solution
    )
        throws IOException {
        System.out.print(SAVE_TEST_CASE_PROMPT);
        if (IO.firstCharOfNextLine(scanner) == 'y') {
            do {
                System.out.print(TEST_CASE_NAME_PROMPT);
                String filePath =
                    Config.TEST_CASES_PATH
                        + scanner.nextLine()
                        + Config.TEST_CASE_FILE_EXTENSION;
                if (fileExists(filePath)) {
                    switch (promptOverwrite(scanner)) {
                        case 'o':
                            Serialize.saveTestCase(filePath, game, solution);
                            return;
                        case 'd':
                            continue;
                        case 's':
                            skipTestCase();
                            return;
                    }
                } else {
                    Serialize.saveTestCase(filePath, game, solution);
                    return;
                }
            } while (true);
        } else {
            skipTestCase();
        }
    }

    /**
     * Returns what the save option selected by the user when the file already
     * exists. 'd' for save under different name, 'o' for overwrite, 's' for
     * skip this test case.
     *
     * Loops if the user does not enter one of those three options.
     *
     * Returns 'd', 'o', or 's'
     */
    private static char promptOverwrite(Scanner scanner) {
        return IO.nextCharInList(scanner, FILE_EXISTS_PROMPT, 'd', 'o', 's');
    }

    private static boolean fileExists(String pathname) {
        File file = new File(pathname);
        return file.exists();
    }

    private static void skipTestCase() {
        System.out.println("Okay, moving on");
    }
}
