package com.mathwithmark.calculatorgamesolver.calculatorgame;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.mathwithmark.calculatorgamesolver.brutesolver.Solver;
import com.mathwithmark.calculatorgamesolver.yaml.Serialize;

public class LevelTests {

    @Test
    void passesAllLevels() {
        for (String testCaseFilename : Helpers.testCaseFilenames()) {
            assertTrue(passesLevel(testCaseFilename), testCaseFilename);
        }
    }

    /**
     * @param testCaseFilename the name of the file to test
     * @return whether the given test case can be solved
     */
    public static boolean passesLevel(String testCaseFilename) {
        TestCase testCase = Serialize.loadTestCase(testCaseFilename);
        List<String> expectedSolutionString = testCase.SOLUTION;

        List<List<String>> solutions = Solver.getAllSolutions(testCase.GAME);

        return solutions.contains(expectedSolutionString);
    }
}
