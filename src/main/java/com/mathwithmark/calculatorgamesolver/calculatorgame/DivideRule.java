package com.mathwithmark.calculatorgamesolver.calculatorgame;

class DivideRule extends Rule {
    @Override
    public CalculatorGame apply(CalculatorGame game) {
        double doubleValue = game.getValue();
        if ((doubleValue / getOperand1()) % 1 != 0) { // if there is a remainder
            return null;
        }
        return CalculatorGame
            .generateGame(
                game.getValue() / getOperand1(),
                game.getGoal(),
                game.getMovesLeft() - 1,
                game.getRules(),
                game.getPortals()
            );
    }

    DivideRule(int operand1) {
        super(Config.DIVIDE, operand1);
    }
}
