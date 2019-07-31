package com.mathwithmark.calculatorgamesolver.brutesolver;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public interface Solver {
    /**
     * Solves the given game, if possible
     * @param game The game to solve
     * @return a list of States, first being the root, last being the solved
     * game. Returns null if the game is unsolveable.
     */
    public static List<State> solve(Game game) {
        Deque<State> stack = new ArrayDeque<>();
        stack.addFirst(game.rootState());

        while (true) {
            if (stack.isEmpty()) {
                return null;
            }
            for (State successor : stack.removeFirst().getSuccessors()) {
                if (successor.getGame().isWon()) {
                    return State.orderedStates(successor);
                }
                stack.addFirst(successor);
            }
        }
    }
}
