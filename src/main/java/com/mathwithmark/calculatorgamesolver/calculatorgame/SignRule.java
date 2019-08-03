package com.mathwithmark.calculatorgamesolver.calculatorgame;

class SignRule extends Rule {
    @Override
    public CalculatorGame apply(CalculatorGame game) {
        return CalculatorGame
            .generateGame(
                -game.getValue(),
                game.getGoal(),
                game.getMovesLeft() - 1,
                game.getRules(),
                game.getPortals()
            );
    }

    SignRule() {
        super(Config.SIGN);
    }
}
