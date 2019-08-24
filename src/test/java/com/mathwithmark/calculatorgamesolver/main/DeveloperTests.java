package com.mathwithmark.calculatorgamesolver.main;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import com.mathwithmark.calculatorgamesolver.brutesolver.Solver;
import com.mathwithmark.calculatorgamesolver.calculatorgame.Config;
import com.mathwithmark.calculatorgamesolver.calculatorgame.Rule;
import com.mathwithmark.calculatorgamesolver.calculatorgame.TestCase;
import com.mathwithmark.calculatorgamesolver.yaml.Serialize;

import org.junit.jupiter.api.Test;

public class DeveloperTests {
    private static final String TEST_DIR_NAME = "test/";
    private static final String TEST_CASE_NAME = TEST_DIR_NAME + "test";

    /** A set of VM arguments for a basic game */
    String[] defaultArgs() {
        int value = 1;
        int goal = 2;
        int moves = 1;
        String[] ruleStrings = Config.ruleString(Rule.ADD, 1).split(" ");
        int[] portals = null;
        return TestUtils.args(value, goal, moves, ruleStrings, portals);
    }

    /** Input for promptSaveTestCase to save under new file name */
    String saveAsNewFileInput() {
        String input = "";
        input += "y\n"; // Yes to save test case
        input += TEST_CASE_NAME + "\n";
        return input;
    }

    /** Complete input from saving one test case and quit */
    String endToEndSaveInput() {
        String input = "";
        input += saveAsNewFileInput();
        input += Developer.QUIT_INPUT + "\n";
        return input;
    }

    /** Create the test directory for 'test' test cases
     * @throws IOException if the command executes poorly
     */
    boolean createTestDirectory() {
        return new File(Config.TEST_CASES_PATH + TEST_DIR_NAME).mkdirs();
    }

    /** Remove the test directory so there are no test artifacts */
    void removeTestDirectory() throws IOException {
        File testDirectory = new File(Config.TEST_CASES_PATH + TEST_DIR_NAME);
        String[] entries = testDirectory.list();
        for (String s : entries) {
            File currentFile = new File(testDirectory.getPath(), s);
            Files.delete(currentFile.toPath());
        }
        testDirectory.delete();
    }

    @Test
    void saveTestCaseHappyPath() {
        IoUtils.prepareEndToEndTest(endToEndSaveInput());

        try {
            createTestDirectory();
            Developer.main(defaultArgs());
            String testCasePath =
                Config.TEST_CASES_PATH
                    + TEST_CASE_NAME
                    + Config.TEST_CASE_FILE_EXTENSION;
            assertTrue(passesLevel(testCasePath));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                removeTestDirectory();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param testCasePath the path of the file to test
     * @return whether the given test case can be solved
     */
    public static boolean passesLevel(String testCasePath) {
        TestCase testCase = Serialize.loadTestCase(testCasePath);
        List<String> expectedSolutionString = testCase.SOLUTION;

        List<List<String>> solutions = Solver.getAllSolutions(testCase.GAME);

        return solutions.contains(expectedSolutionString);
    }
}
