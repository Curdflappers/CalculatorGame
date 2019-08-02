package com.mathwithmark.calculatorgamesolver.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import com.mathwithmark.calculatorgamesolver.brutesolver.Solver;
import com.mathwithmark.calculatorgamesolver.calculatorgame.CalculatorGame;
import com.mathwithmark.calculatorgamesolver.calculatorgame.Config;
import com.mathwithmark.calculatorgamesolver.yaml.Serialize;

public class Developer {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        do {
            Main.getInput(args, scanner);
            List<List<String>> solutions =
                Solver.getAllSolutions(Main.getCalculatorGame());
            List<String> solution = solutions.get(0);
            Main.printSolutions(solutions);
            promptSaveTestCase(scanner, Main.getCalculatorGame(), solution);
            System.out
                .print(
                    "Enter '"
                        + Config.QUIT
                        + "' to quit. Enter anything else to continue: "
                );
        } while (!scanner.nextLine().equalsIgnoreCase(Config.QUIT));
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
        System.out.print("Save test case (y/n): ");
        String saveResponse = scanner.nextLine();
        if (saveResponse.length() == 0 || saveResponse.charAt(0) == 'y') {
            do {
                System.out.print("Filename (\".yaml\" will be added): ");
                String filePath =
                    Config.TEST_CASES_PATH + "/" + scanner.nextLine() + ".yaml";
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
     * exists. 'o' for overwrite, 'd' for save under different name, 's' for
     * skip this test case.
     *
     * Loops if the user does not enter one of those three options.
     *
     * Returns 'o', 'd', or 's'
     */
    private static char promptOverwrite(Scanner scanner) {
        char firstChar = '\n';

        while (!validOverwriteResponse(firstChar)) {
            System.out
                .print(
                    "That file already exists. [O]verwrite it? Save under [D]ifferent name? [S]kip this test case? (o/d/s): "
                );

            String response = scanner.nextLine();

            if (response.length() == 0) {
                System.out.println("Please enter a response.");
                continue;
            }

            if (response.length() != 1) {
                System.out.println("Please enter a one-character response");
                continue;
            }

            firstChar = Character.toLowerCase(response.charAt(0));

            if (!validOverwriteResponse(firstChar)) {
                System.out
                    .println(
                        "Please enter 'o', 'd', or 's' only (case-insensitive)"
                    );
            }
        }

        return firstChar;
    }

    /**
     * Returns whether the given character is a valid response for the overwrite
     * prompt
     */
    private static boolean validOverwriteResponse(char c) {
        return c == 'o' || c == 'd' || c == 's';
    }

    private static boolean fileExists(String filename) {
        File file = new File(filename);
        Scanner input = null;
        try {
            input = new Scanner(file);
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } finally {
            if (input != null) input.close();
        }
    }

    private static void skipTestCase() {
        System.out.println("Okay, moving on");
    }
}
