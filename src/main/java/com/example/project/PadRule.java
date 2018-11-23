package com.example.project;

public class PadRule extends Rule {
    public Game apply(Game game) {
        String valString = String.valueOf((int) game.getValue());
        valString += getOperand1();
        return new Game(
            Double.parseDouble(valString),
            game.getGoal(),
            game.getMovesLeft() - 1,
            game.getValidRules()
        );
    }

    public PadRule(int operand1) {
        super(Config.PAD, operand1);
    }
}
