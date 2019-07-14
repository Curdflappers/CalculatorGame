package base;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public interface Solver {
    /**
     * Run a DFS and return the ordered states the represent the solution to the
     * game
     * @param game The game to solve
     * @return a list of States, first being the root, last being the solved
     * game
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
