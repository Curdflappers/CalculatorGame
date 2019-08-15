package com.mathwithmark.calculatorgamesolver.calculatorgame;

class SubtractRule extends Rule {
    @Override
    public CalculatorGame apply(CalculatorGame game) {
        return makeCalculatorGame(
            game.getValue() - getOperand1(),
            game.getGoal(),
            game.getMovesLeft() - 1,
            game.getRules(),
            game.getPortals()
        );
    }

    SubtractRule(int operand1) {
        super(SUBTRACT, operand1);
    }
}
