package base;

import java.util.ArrayDeque;
import java.util.Deque;

public class Solver {
    /**
     * Run a DFS and return the State that contains the end game.
     */
    static State solve(Game game) {
        Deque<State> stack = new ArrayDeque<>();
        stack.addFirst(game.rootState());

        while (true) {
            if (stack.isEmpty()) {
                return null;
            }
            for (State successor : stack.removeFirst().getSuccessors()) {
                if (successor.getGame().isWon()) {
                    return successor;
                }
                stack.addFirst(successor);
            }
        }
    }
}
