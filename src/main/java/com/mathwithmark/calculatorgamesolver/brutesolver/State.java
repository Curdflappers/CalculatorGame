package com.mathwithmark.calculatorgamesolver.brutesolver;

import java.util.List;

/**
 * A wrapper class for implementations of the Game interface. Used for solving
 * games, it tracks parent states and transition descriptions (how this state
 * came to be from its parent)
 */
public class State {
    /** The game information for this state */
    private final Game GAME;
    /** The parent state to which <code>rule</code> was used */
    private final State PARENT;
    /** Describes the transition from parent to this. Null if no parent */
    private final String TRANSITION_STRING;

    /**
     * Creates a new State with the given <code>game</code>. <code>rule</code>
     * and <code>parent</code> are <code>null</code>.
     */
    public State(Game game) {
        this.GAME = game;
        this.PARENT = null;
        this.TRANSITION_STRING = null;
    }

    public State(Game game, State parent, String transitionString) {
        this.GAME = game;
        this.PARENT = parent;
        this.TRANSITION_STRING = transitionString;
    }

    public Game getGame() {
        return GAME;
    }

    public State getParent() {
        return PARENT;
    }

    public String getTransitionString() {
        return TRANSITION_STRING;
    }

    /**
     * Determines whether the given game is redundant in the ancestor chain of
     * this. The given game is redundant if it is roughly equivalent to the game
     * of this or any of its ancestors. The definition of roughly equivalent
     * varies based on the type of game.
     * @param successorGame the game to test
     * @return Whether successorGame is redundant
     */
    public boolean redundant(Game successorGame) {
        State ancestor = this;
        while (ancestor != null) {
            if (successorGame.roughlyEquals(ancestor.getGame())) return true;
            ancestor = ancestor.getParent();
        }
        return false;
    }

    /**
     * Generates non-redundant successor states based on its game
     * @return all non-redundant successor states
     */
    public List<State> getSuccessors() {
        List<State> successors = GAME.getSuccessors(this);
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
