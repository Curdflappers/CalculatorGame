package com.example.project;

public class ConvertRule extends Rule {
    public Game apply(Game game) {
        double value = game.getValue();
        String valString = String.valueOf((int) value);
        String op1String = String.valueOf(getOperand1());
        String op2String = String.valueOf(getOperand2());
        valString = valString.replace(op1String, op2String);
        double newValue = Double.parseDouble(valString);
        return new Game(
            newValue,
            game.getGoal(),
            game.getMovesLeft() - 1,
            game.getValidRules()
        );
    }

    public ConvertRule(int operator, int operand1, int operand2) {
        super(operator, operand1, operand2);
    }
}
