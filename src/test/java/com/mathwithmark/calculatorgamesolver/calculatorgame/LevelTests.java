package com.mathwithmark.calculatorgamesolver.calculatorgame;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.mathwithmark.calculatorgamesolver.brutesolver.Solver;
import com.mathwithmark.calculatorgamesolver.brutesolver.State;
import com.mathwithmark.calculatorgamesolver.yaml.Serialize;

public class LevelTests {

    @Test
    void passesAllLevels() {
        for (String testCaseFilename : Helpers.testCaseFilenames()) {
            assertTrue(passesLevel(testCaseFilename), testCaseFilename);
        }
    }

    private static List<String> solutionStrings(List<List<State>> solutions) {
        List<String> solutionStrings = new ArrayList<>();
        for (List<State> solution : solutions) {
            solutionStrings.add(State.allTransitions(solution).toString());
        }
        return solutionStrings;
    }

    /**
     * @param testCaseFilename the name of the file to test
     * @return whether the given test case can be solved
     */
    public static boolean passesLevel(String testCaseFilename) {
        TestCase testCase = Serialize.loadTestCase(testCaseFilename);
        String expectedSolutionString = testCase.SOLUTION.toString();

        List<List<State>> solutions = Solver.getAllSolutions(testCase.GAME);
        List<String> actualSolutionStrings = solutionStrings(solutions);

        return actualSolutionStrings.contains(expectedSolutionString);
    }
}
