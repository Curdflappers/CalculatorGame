package com.mathwithmark.calculatorgamesolver.calculatorgame;

class PowerRule extends Rule {
    @Override
    public CalculatorGame apply(CalculatorGame game) {
        return CalculatorGame
            .generateGame(
                (int) Math.pow(game.getValue(), getOperand1()),
                game.getGoal(),
                game.getMovesLeft() - 1,
                game.getRules(),
                game.getPortals()
            );
    }

    PowerRule(int operand1) {
        super(Config.POWER, operand1);
    }
}
