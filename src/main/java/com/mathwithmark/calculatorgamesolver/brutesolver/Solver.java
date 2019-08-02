package com.mathwithmark.calculatorgamesolver.brutesolver;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public interface Solver {
    /**
     * Returns all solutions to the given game.
     * @param game The game to solve
     * @return a list of lists of States. In each sublist, the first State is
     * the root, the last is the solved game. Returns an empty list if the game
     * is unsolveable.
     */
    public static List<List<State>> getAllSolutions(Game game) {
        Deque<State> stack = new ArrayDeque<>();
        List<List<State>> solutions = new ArrayList<>();
        stack.addFirst(game.rootState());

        while (true) {
            if (stack.isEmpty()) {
                return solutions;
            }
            for (State successor : stack.removeFirst().getSuccessors()) {
                if (successor.getGame().isWon()) {
                    solutions.add(State.orderedStates(successor));
                }
                stack.addFirst(successor);
            }
        }
    }
}
