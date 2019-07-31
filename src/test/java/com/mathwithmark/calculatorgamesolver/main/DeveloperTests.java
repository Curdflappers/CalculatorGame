package com.mathwithmark.calculatorgamesolver.main;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;

import com.mathwithmark.calculatorgamesolver.calculatorgame.Config;

import org.junit.jupiter.api.Test;

public class DeveloperTests {
    private static final String TEST_DIR = "test";

    /** A set of VM arguments for a basic game */
    String[] defaultArgs() {
        int value = 1;
        int goal = 2;
        int moves = 1;
        String[] ruleStrings = {
            Config.ruleString(Config.ADD, 1)
        };
        int[] portals = null;
        return TestUtils.args(value, goal, moves, ruleStrings, portals);
    }

    /** Input for promptSaveTestCase to save under new file name */
    String saveAsNewFileInput() {
        String input = "";
        String testCaseName =
            TEST_DIR + "/" + String.valueOf(System.currentTimeMillis());
        input += "y\n"; // Save test case
        input += testCaseName + "\n"; // Name of test case
        return input;
    }

    /** Complete input from saving one test case and quit */
    String endToEndSaveInput() {
        String input = "";
        input += saveAsNewFileInput();
        input += Config.QUIT;
        return input;
    }

    /** Create the test directory for 'test' test cases
     * @throws IOException if the command executes poorly
     */
    boolean createTestDirectory() {
        return new File(Config.TEST_CASES_PATH + "/" + TEST_DIR).mkdirs();
    }

    /** Remove the test directory so there are no test artifacts */
    void removeTestDirectory() throws IOException {
        File directory = new File(Config.TEST_CASES_PATH + "/" + TEST_DIR);
        String[] entries = directory.list();
        for (String s : entries) {
            File currentFile = new File(directory.getPath(), s);
            Files.delete(currentFile.toPath());
        }
        directory.delete();
    }

    private boolean testDirHasFile() {
        File directory = new File(Config.TEST_CASES_PATH + "/" + TEST_DIR);
        return directory.list().length == 1;
    }

    @Test
    void saveTestCaseHappyPath() {
        PrintStream out = System.out;
        IoUtils.prepareEndToEndTest(endToEndSaveInput());

        try {
            createTestDirectory();
            Developer.main(defaultArgs());
            assertTrue(testDirHasFile());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                removeTestDirectory();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.setOut(out);
        }
    }
}
