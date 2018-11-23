package com.example.project;

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
