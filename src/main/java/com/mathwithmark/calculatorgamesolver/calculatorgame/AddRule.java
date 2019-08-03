package com.mathwithmark.calculatorgamesolver.calculatorgame;

class AddRule extends Rule {
    @Override
    public CalculatorGame apply(CalculatorGame game) {
        return CalculatorGame
            .generateGame(
                game.getValue() + getOperand1(),
                game.getGoal(),
                game.getMovesLeft() - 1,
                game.getRules(),
                game.getPortals()
            );
    }

    AddRule(int operand1) {
        super(Config.ADD, operand1);
    }
}
