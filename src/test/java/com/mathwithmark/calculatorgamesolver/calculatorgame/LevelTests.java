package com.mathwithmark.calculatorgamesolver.calculatorgame;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.mathwithmark.calculatorgamesolver.brutesolver.Solver;
import com.mathwithmark.calculatorgamesolver.brutesolver.State;

public class LevelTests {

    @Test
    void passesAllLevels() {
        boolean success = true;
        for (String testCase : Helpers.testCases()) {
            System.out.print("Testing " + testCase + ": ");
            String expectedSolution = Helpers.expectedSolution(testCase);
            CalculatorGame game = Helpers.loadLevel(testCase);

            if (game == null) {
                success = false;
                System.out.println("FAILED: Could not load level");
                continue;
            }

            List<State> solutionStates = Solver.solve(game);
            String solutionString = State.allTransitions(solutionStates);

            if (!solutionString.equals(expectedSolution)) {
                success = false;
                System.out.println("FAILED");
                System.out.println("Expected:\n" + expectedSolution);
                System.out.println("Actual:\n" + solutionString);
                continue;
            }

            System.out.println("PASSED");
        }

        assertTrue(success);
    }
}
