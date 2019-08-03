package com.mathwithmark.calculatorgamesolver.calculatorgame;

class MultiplyRule extends Rule {
    @Override
    public CalculatorGame apply(CalculatorGame game) {
        return CalculatorGame
            .generateGame(
                game.getValue() * getOperand1(),
                game.getGoal(),
                game.getMovesLeft() - 1,
                game.getRules(),
                game.getPortals()
            );
    }

    MultiplyRule(int operand1) {
        super(Config.MULTIPLY, operand1);
    }
}
