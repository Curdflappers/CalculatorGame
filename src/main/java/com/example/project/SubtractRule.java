package com.example.project;

public class SubtractRule extends Rule {
    public Game apply(Game game) {
        return new Game(
            game.getValue() - getOperand1(),
            game.getGoal(),
            game.getMovesLeft() - 1,
            game.getValidRules()
        );
    }

    public SubtractRule(int operand1) {
        super(Config.SUBTRACT, operand1);
    }
}
