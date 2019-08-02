package com.mathwithmark.calculatorgamesolver.brutesolver;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public final class Solver {
    /**
     * Returns all solutions to the given game.
     * @param game The game to solve
     * @return a list of lists of States. In each sublist, the first State is
     * the root, the last is the solved game. Returns an empty list if the game
     * is unsolveable.
     */
    public static List<List<String>> getAllSolutions(Game game) {
        Deque<State> stack = new ArrayDeque<>();
        List<List<String>> solutions = new ArrayList<>();
        stack.addFirst(game.rootState());

        while (true) {
            if (stack.isEmpty()) {
                return solutions;
            }
            for (State successor : stack.removeFirst().getSuccessors()) {
                if (successor.getGame().isWon()) {
                    solutions.add(solutionFrom(successor));
                }
                stack.addFirst(successor);
            }
        }
    }

    /**
     * Returns all ancestor states and endState ordered by ancestry, with the
     * oldest ancestor (root) at index 0 and the given endState at the end.
     *
     * @param endState the terminal state
     * @return an ordered list of ancestor states from root to endState
     */
    private static List<State> orderedStates(State endState) {
        List<State> states = new LinkedList<>();
        while (endState != null) {
            states.add(0, endState); // add more recent to beginning of list
            endState = endState.getParent();
        }
        return states;
    }

    /**
     * Returns a string that represents all transitions from one state to the
     * next to the game. Each transition string is separated by a newline
     * character.
     */
    private static List<String> solutionFrom(State state) {
        List<State> states = orderedStates(state);
        List<String> transitionStrings = new ArrayList<>();
        for (State element : states) {
            if (element.getParent() != null) {
                transitionStrings.add(element.getTransitionString());
            }
        }
        return transitionStrings;
    }
}
