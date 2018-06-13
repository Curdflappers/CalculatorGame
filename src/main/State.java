package main;

public class State {
    private Rule rule;
    private int value, goal, movesLeft;
    private State parent;

    public State(Rule rule, int value, int goal, int movesLeft, State parent) {
        this.rule = rule;
        this.value = value;
        this.goal = goal;
        this.movesLeft = movesLeft;
        this.parent = parent;
    }

    public State(State state, Rule rule) {
        this.movesLeft = state.getMovesLeft() - 1;
        this.goal = state.goal;
        this.value = Game.applyRule(rule, state.getValue());
        this.rule = rule;
        this.parent = state;
    }

    public Rule getRule() {
        return rule;
    }

    public int getValue() {
        return value;
    }

    public int getGoal() {
        return goal;
    }

    public int getMovesLeft() {
        return movesLeft;
    }

    public State getParent() {
        return parent;
    }
}
