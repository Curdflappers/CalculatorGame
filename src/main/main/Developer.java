package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import brutesolver.Solver;
import brutesolver.State;
import rules.CalculatorGame;
import rules.Config;

public class Developer {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        do {
            Main.getInput(new String[] {}, scanner);
            System.out.println(Config.SOLUTION_PROMPT);
            List<State> solution = Solver.solve(Main.getCalculatorGame());
            String solutionString = State.allTransitions(solution);
            System.out.print(solutionString);
            promptSaveTestCase(
                scanner,
                Main.getCalculatorGame(),
                solutionString
            );
        } while (true);
    }

    /**
     * Prompt the user to see if they want to save the test case
     * @param scanner
     */
    private static void promptSaveTestCase(
        Scanner scanner,
        CalculatorGame game,
        String solution
    ) {
        System.out.print("Save test case (y/n): ");
        String saveResponse = scanner.nextLine();
        if (saveResponse.length() == 0 || saveResponse.charAt(0) == 'y') {
            do {
                System.out.print("Filename (\".cgl\" will be added): ");
                String filename =
                    Config.TESTCASES_PATH + "/" + scanner.nextLine() + ".cgl";
                if (fileExists(filename)) {
                    switch (promptOverwrite(scanner)) {
                        case 'o':
                            saveTestCase(filename, game, solution);
                            return;
                        case 'd':
                            continue;
                        case 's':
                            skipTestCase();
                            return;
                    }
                } else {
                    saveTestCase(filename, game, solution);
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

    private static void saveTestCase(
        String filename,
        CalculatorGame game,
        String solution
    ) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(filename);

            // Newlines automatically included as part of printed strings
            writer.print(game);
            writer.print(solution);
        } catch (FileNotFoundException e) {
            System.err.println("Unable to write to " + filename);
        } finally {
            if (writer != null) writer.close();
        }
    }

    private static void skipTestCase() {
        System.out.println("Okay, moving on");
    }
}
