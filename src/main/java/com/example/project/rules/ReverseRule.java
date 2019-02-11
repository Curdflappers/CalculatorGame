package com.example.project.rules;

import com.example.project.base.Config;
import com.example.project.base.Game;

public class ReverseRule extends Rule {
    public Game apply(Game game) {
        boolean negative = game.getValue() < 0;
        String valString = String.valueOf((int) game.getValue());
        if (negative) {
            valString = valString.substring(1); // shave off minus sign
        }
        // Reverse the string
        valString = new StringBuilder(valString).reverse().toString();
        double newValue = Double.parseDouble(valString);
        newValue = negative ? -newValue : newValue; // fix the sign
        return new Game(
            newValue,
            game.getGoal(),
            game.getMovesLeft() - 1,
            game.getValidRules()
        );
    }

    public ReverseRule() {
        super(Config.REVERSE);
    }
}
