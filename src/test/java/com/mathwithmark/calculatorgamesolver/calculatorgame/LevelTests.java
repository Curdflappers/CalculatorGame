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
            List<String> expectedSolution =
                Serialize.loadSolution(testCase);
            CalculatorGame game = Serialize.loadGame(testCase);

            if (game == null) {
                success = false;
                System.out.println("FAILED: Could not load level");
                continue;
            }

            List<State> solutionStates = Solver.solve(game);
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
