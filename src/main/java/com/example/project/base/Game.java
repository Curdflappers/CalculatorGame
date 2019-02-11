package com.example.project.base;

import java.util.Arrays;

import com.example.project.rules.Rule;

public class Game {
    /** The current number for this game */
    private double value;

    /** The goal number for this game */
    private int goal;

    /** The moves left in this game */
    private int movesLeft;

    /** The rules that can be used in this game */
    private Rule[] validRules;

    /**
     * Create a game of the given parameters
     *
     * @param value The start state
     * @param goal The end state
     * @param moves The number of moves to be used
     * @param rules The rules that can be used
     */
    public Game(double value, int goal, int moves, Rule[] rules) {
        this.value = value;
        this.goal = goal;
        this.movesLeft = moves;
        this.validRules = rules;
    }

    public double getValue() {
        return value;
    }

    public int getGoal() {
        return goal;
    }

    public int getMovesLeft() {
        return movesLeft;
    }

    public boolean isValidRule(Rule rule) {
        return Arrays.asList(validRules).contains(rule);
    }

    public State getState() {
        return new State(this);
    }

    /** The valid rules for this game */
    public Rule[] getValidRules() {
        return Arrays.copyOf(validRules, validRules.length);
    }

    public boolean equals(Object other) {
        if (other instanceof Game) {
            Game otherGame = (Game) other;
            return otherGame.getValue() == getValue()
                && otherGame.getGoal() == getGoal()
                && otherGame.getMovesLeft() == getMovesLeft()
                && Arrays.equals(otherGame.getValidRules(), getValidRules());
        } else
            return false;
    }
}
