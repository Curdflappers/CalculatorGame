package com.example.project.rules;

import java.util.Arrays;

import com.example.project.Config;
import com.example.project.Game;

/**
 * Operates similar to the PadRule, but employs the update method to change the
 * value to be padded.
 */
public class StoreRule extends Rule {
    final boolean INITIALIZED;

    /**
     * Updates the value to be padded to match the value of the game.
     */
    public Game update(Game game) {
        // No change would be made, return the original game
        if (INITIALIZED && game.getValue() == getOperand1()) return game;

        // Replace this with initialized version and update successor game
        StoreRule newRule = new StoreRule((int) game.getValue());
        Rule[] newValidRules = game.getValidRules();
        int index = Arrays.asList(newValidRules).indexOf(this);
        newValidRules[index] = newRule;
        return new Game(
            game.getValue(),
            game.getGoal(),
            game.getMovesLeft(),
            newValidRules
        );
    }

    public Game apply(Game game) {
        // Do nothing if uninitialized or set to pad negative
        if (!INITIALIZED || getOperand1() < 0) return game;

        // pad the value
        String valString = String.valueOf((int) game.getValue());
        valString += getOperand1();
        return new Game(
            Double.parseDouble(valString),
            game.getGoal(),
            game.getMovesLeft() - 1,
            game.getValidRules()
        );
    }

    public StoreRule() {
        super(Config.STORE);
        INITIALIZED = false;
    }

    public StoreRule(int value) {
        super(Config.STORE, value);
        INITIALIZED = true;
    }

    @Override
    public boolean equals(Object other) {
        if (super.equals(other)) {
            StoreRule otherStoreRule = (StoreRule) other;
            return INITIALIZED == otherStoreRule.INITIALIZED;
        }
        return false;
    }
}
