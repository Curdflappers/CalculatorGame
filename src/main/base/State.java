package base;

import java.util.List;

import rules.Rule;

/**
 * A wrapper class for implementations of the Game interface. Used for solving
 * games, it tracks parent states and transition descriptions (how this state
 * came to be from its parent)
 */
public class State {
    /** The game information for this state */
    private final CalculatorGame game; // TODO make abstract
    /** The parent state to which <code>rule</code> was used */
    private final State parent;
    /** Describes the transition from parent to this. Null if no parent */
    private final String transitionString;

    /**
     * Creates a new State with the given <code>game</code>, <code>rule</code>
     * and <code>parent</code> are <code>null</code>.
     */
    public State(CalculatorGame game) { // TODO abstract
        this.game = game;
        this.parent = null;
        this.transitionString = null;
    }

    /**
     * Creates a new state, a child of the given state, a result of applying or
     * updating the given rule
     * @param parent
     * @param rule
     * @param applied True if this state is the result of applying the given
     * rule to the given state, false if this state is the result of updating
     * the given rule
     */
    public State(CalculatorGame game, State parent, String transitionString) {
        this.game = game;
        this.parent = parent;
        this.transitionString = transitionString;
    }

    public CalculatorGame getGame() {
        return game;
    }

    public State getParent() {
        return parent;
    }

    public String getTransitionString() {
        return transitionString;
    }

    public double getValue() { // TODO remove
        return game.getValue();
    }

    public int getGoal() { // TODO remove
        return game.getGoal();
    }

    public int getMovesLeft() { // TODO remove
        return game.getMovesLeft();
    }

    public Rule[] getRules() { // TODO remove
        return game.getRules();
    }

    /**
     * Determines whether the given game is redundant in the ancestor chain of
     * this. The given game is redundant if it is roughly equivalent to the game
     * of this or any of its ancestors. The definition of roughly equivalent
     * varies based on the type of game.
     * @param successorGame the game to test
     * @return Whether successorGame is redundant
     */
    // TODO abstract
    public boolean redundant(CalculatorGame successorGame) {
        State ancestor = this;
        while (ancestor != null) {
            if(successorGame.equalsExceptMoves(ancestor.getGame())) {
                return true;
            }
            ancestor = ancestor.getParent();
        }
        return false;
    }

    /**
     * Generates non-redundant successor states based on its game
     * @return all non-redundant successor states
     */
    public List<State> getSuccessors() {
        List<State> successors = game.getSuccessors(this);
        for (int i = 0; i < successors.size(); i++) {
            State potentialSuccessor = successors.get(i);
            if (redundant(potentialSuccessor.getGame())) {
                successors.remove(i);
                i--;
            }
        }
        return successors;
    }
}
