package com.example.project.rules;

import com.example.project.base.Config;
import com.example.project.base.Game;

public class DivideRule extends Rule {
    public Game apply(Game game) {
        return new Game(
            game.getValue() / getOperand1(),
            game.getGoal(),
            game.getMovesLeft() - 1,
            game.getValidRules()
        );
    }

    public DivideRule(int operand1) {
        super(Config.DIVIDE, operand1);
    }
}
