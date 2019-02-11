package com.example.project.rules;

import com.example.project.Config;
import com.example.project.Game;

public class PowerRule extends Rule {
    public Game apply(Game game) {
        return new Game(
            Math.pow(game.getValue(), getOperand1()),
            game.getGoal(),
            game.getMovesLeft() - 1,
            game.getValidRules()
        );
    }

    public PowerRule(int operand1) {
        super(Config.POWER, operand1);
    }
}
