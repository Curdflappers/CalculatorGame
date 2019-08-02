package com.mathwithmark.calculatorgamesolver.calculatorgame;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.mathwithmark.calculatorgamesolver.brutesolver.Solver;
import com.mathwithmark.calculatorgamesolver.yaml.Serialize;

public class LevelTests {

    @Test
    void passesAllLevels() {
        for (String testCasePath : Helpers.testCasePaths()) {
            assertTrue(passesLevel(testCasePath), testCasePath);
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
