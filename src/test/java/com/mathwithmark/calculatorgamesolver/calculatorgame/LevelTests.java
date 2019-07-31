package com.mathwithmark.calculatorgamesolver.calculatorgame;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.mathwithmark.calculatorgamesolver.brutesolver.Solver;
import com.mathwithmark.calculatorgamesolver.brutesolver.State;
import com.mathwithmark.calculatorgamesolver.yaml.Serialize;

public class LevelTests {

    @Test
    void passesAllLevels() {
        boolean success = true;
        for (String testCaseString : Helpers.testCaseStrings()) {
            TestCase testCase = Serialize.loadTestCase(testCaseString);
            List<String> expectedSolution = testCase.SOLUTION;

            List<State> solutionStates = Solver.solve(testCase.GAME);
            List<String> actualSolution = State.allTransitions(solutionStates);

            if (!actualSolution.equals(expectedSolution)) {
                success = false;
                System.out.println("FAILED");
                System.out.println("Expected:\n" + expectedSolution);
                System.out.println("Actual:\n" + actualSolution);
            }
        }

        assertTrue(success);
    }
}
