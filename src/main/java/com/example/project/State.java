package com.example.project;

public class State {
    /** The game information for this state */
    private Game game;
    /** The rule that was applied to <code>parent</code> to get to this state */
    private Rule rule;
    /** The parent state to which <code>rule</code> was applied */
    private State parent;

    /**
     * Creates a new State with the given <code>game</code>, <code>rule</code>
     * and <code>parent</code> are <code>null</code>.
     */
    public State(Game game) {
        this.game = game;
        this.rule = null;
        this.parent = null;
    }

    /**
     * Creates a new state, a child of the given state, a result of applying
     * the given rule
     * @param state
     * @param rule
     */
    public State(State state, Rule rule) {
        int movesLeft = state.getMovesLeft() - 1;
        double value = rule.apply(state.getValue());
        this.game =
            new Game(value, state.getGoal(), movesLeft, state.getRules());
        this.rule = rule;
        this.parent = state;
    }

    public Rule getRule() {
        return rule;
    }

    public double getValue() {
        return game.getValue();
    }

    public int getGoal() {
        return game.getGoal();
    }

    public int getMovesLeft() {
        return game.getMovesLeft();
    }

    public State getParent() {
        return parent;
    }

    public Rule[] getRules() {
        return game.getValidRules();
    }
}
