package base;

import java.util.List;

import game.Solver;
import game.State;

public class CGSolver {
    /**
     * Solves the game and returns the terminal state
     * @param calculatorGame the game to solve
     * @return the solved game, wrapped in a State
     */
    static List<State> solve(CalculatorGame calculatorGame) {
        List<State> states = Solver.solve(calculatorGame);
        cleanUp(states);
        return states;
    }

    /**
     * Removes extra "Update Store"s that clutter the solution
     * @param states The ordered list of states to clean up
     */
    public static void cleanUp(List<State> states) {
        boolean foundOne = false;
        int foundIndex = -1;

        // skip first state, it has a null rule
        for (int i = 1; i < states.size(); i++) {
            State state = states.get(i);
            // TODO oh so gross
            if (
                state
                    .getTransitionString()
                    .endsWith(Config.ruleString(Config.STORE))
            ) {
                if (
                    state.getTransitionString().startsWith(Config.APPLY_PROMPT)
                ) {
                    foundOne = false;
                } else if (foundOne) { // already found one, this is second

                    states.remove(foundIndex);
                    i--;
                    foundIndex = i;
                } else { // haven't found one yet, don't remove this one yet
                    foundOne = true;
                    foundIndex = i;
                }
            }
        }

        // Remove potential trailing "Update Store"s
        if (foundOne) states.remove(foundIndex);
    }
}
