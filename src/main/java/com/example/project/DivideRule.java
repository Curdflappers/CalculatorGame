package com.example.project;

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
