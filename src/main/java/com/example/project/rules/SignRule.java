package com.example.project.rules;

import com.example.project.base.Config;
import com.example.project.base.Game;

public class SignRule extends Rule {
    public Game apply(Game game) {
        return new Game(
            -game.getValue(),
            game.getGoal(),
            game.getMovesLeft() - 1,
            game.getValidRules()
        );
    }

    public SignRule() {
        super(Config.SIGN);
    }
}
