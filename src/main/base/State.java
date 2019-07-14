package base;

import rules.Rule;

/**
 * A wrapper class for implementations of the Game interface. Used for solving
 * games, it tracks parent states and transition descriptions (how this state
 * came to be from its parent)
 */
public class State {
    /** The game information for this state */
    private CalculatorGame game; // TODO make abstract
    /** The rule that was used on <code>parent</code> to get to this state */
    private Rule rule; // TODO change to string that is transition description
    /** The parent state to which <code>rule</code> was used */
    private State parent;
    /** Whether the rule was applied or updated (true iff applied) */
    private boolean applied;

    /**
     * Creates a new State with the given <code>game</code>, <code>rule</code>
     * and <code>parent</code> are <code>null</code>.
     */
    public State(CalculatorGame game) { // TODO abstract
        this.game = game;
        this.rule = null;
        this.parent = null;
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
    public State(State parent, Rule rule, boolean applied) {
        this.applied = applied;
        if (applied) { // TODO move this logic to game implementing class
            this.game = rule.apply(parent.getGame());
        } else {
            this.game = rule.update(parent.getGame());
        }
        this.rule = rule;
        this.parent = parent;
    }

    public Rule getRule() { // TODO remove
        return rule;
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

    public State getParent() {
        return parent;
    }

    public Rule[] getRules() { // TODO remove
        return game.getValidRules();
    }

    public CalculatorGame getGame() {
        return game;
    }

    public boolean getApplied() { // TODO remove
        return applied;
    }
}
