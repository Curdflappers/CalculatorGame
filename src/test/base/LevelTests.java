package base;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import game.State;
import rules.Rule;

public class LevelTests {

    private static CalculatorGame game = null;
    private static String expectedSolutionString = null;

    private static void loadLevel(String level) {
        File file = new File(Config.TESTCASES_PATH + "/" + level);
        Scanner input = null;
        try {
            input = new Scanner(file);

            int value = Integer.parseInt(input.nextLine());
            int goal = Integer.parseInt(input.nextLine());
            int moves = Integer.parseInt(input.nextLine());

            String currentRuleString;
            List<Rule> rules = new ArrayList<>();

            // While the current rule is not the empty string
            while (!(currentRuleString = input.nextLine()).equals("")) {
                Rule rule = Rule.ruleFromString(currentRuleString);
                rules.add(rule);
            }

            boolean portalsPresent = input.nextLine().charAt(0) == 'y';

            int leftPortal = -1;
            int rightPortal = -1;

            if (portalsPresent) {
                leftPortal = Integer.parseInt(input.nextLine());
                rightPortal = Integer.parseInt(input.nextLine());
            }

            expectedSolutionString = "";

            while (input.hasNextLine()) {
                expectedSolutionString += input.nextLine() + "\n";
            }

            int[] portals = null;
            if (portalsPresent) portals = new int[] {
                leftPortal, rightPortal
            };

            game =
                new CalculatorGame(
                    value,
                    goal,
                    moves,
                    rules.toArray(new Rule[rules.size()]),
                    portals
                );

        } catch (FileNotFoundException e) {
            game = null;
            System.out.print("FILE NOT FOUND");
        } finally {
            if (input != null) input.close();
        }
    }

    @Test
    void passesAllLevels() {

        boolean failed = false;

        for (String testCase : testCases()) {
            System.out.print("Testing " + testCase + ": ");
            loadLevel(testCase);

            if (game == null) {
                failed = true;
                System.out.println(" FAILED");
                continue;
            }

            List<State> solutionStates = CGSolver.solve(game);
            String solutionString = State.allTransitions(solutionStates);

            if (!solutionString.equals(expectedSolutionString)) {
                failed = true;
                System.out.println("FAILED");
                System.out.println("Expected:\n" + expectedSolutionString);
                System.out.println("Actual:\n" + solutionString);
                continue;
            }

            System.out.println("PASSED");
        }

        assertFalse(failed);
    }

    private static List<String> testCases() {
        final File folder = new File(Config.TESTCASES_PATH);
        List<String> filenames = new ArrayList<>();

        for (final File fileEntry : folder.listFiles()) {
            filenames.add(fileEntry.getName());
        }

        return filenames;
    }
}
