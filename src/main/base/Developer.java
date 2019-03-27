package base;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Developer {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        do {
            Main.getInput(new String[] {}, scanner);
            System.out.println(Config.SOLUTION_PROMPT);
            State endState = Main.solveGame(Main.getGame());
            String solution = Main.extractSolution(endState);
            System.out.print(solution);
            promptSaveTestCase(scanner, Main.getGame(), solution);
        } while (true);
    }

    /**
     * Prompt the user to see if they want to save the test case
     * @param scanner
     */
    private static void promptSaveTestCase(
        Scanner scanner,
        Game game,
        String solution
    ) {
        System.out.print("Save test case (y/n): ");
        String saveResponse = scanner.nextLine();
        if (saveResponse.length() == 0 || saveResponse.charAt(0) == 'y') {
            System.out.print("Filename (\".cgl\" will be added): ");
            String filename =
                Config.TESTCASES_PATH + "/" + scanner.nextLine() + ".cgl";

            if (fileExists(filename)) {
                System.out
                    .print("That file already exists. Overwrite it? (y/n): ");
                if (scanner.nextLine().charAt(0) == 'y') {
                    saveTestCase(filename, game, solution);
                } else {
                    skipTestCase();
                }
            } else {
                saveTestCase(filename, game, solution);
            }
        } else {
            skipTestCase();
        }
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
        Game game,
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
