package com.example.project;

public class State {
    /** The game information for this state */
    private Game game;
    /** The rule that was used on <code>parent</code> to get to this state */
    private Rule rule;
    /** The parent state to which <code>rule</code> was used */
    private State parent;
    /** Whether the rule was applied or updated (true iff applied) */
    private boolean applied;

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
     * Creates a new state, a child of the given state, a result of applying or
     * updating the given rule
     * @param state
     * @param rule
     * @param apply True if this state is the result of applying the given rule
     * to the given state, false if this state is the result of updating the
     * given rule
     */
    public State(State state, Rule rule, boolean apply) {
        applied = apply;
        if (apply) {
            this.game = rule.apply(state.getGame());
        } else {
            this.game = rule.update(state.getGame());
        }
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

    public Game getGame() {
        return game;
    }

    public boolean getApplied() {
        return applied;
    }
}
