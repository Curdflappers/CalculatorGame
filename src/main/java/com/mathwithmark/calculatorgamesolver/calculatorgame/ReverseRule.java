package com.mathwithmark.calculatorgamesolver.calculatorgame;

class ReverseRule extends Rule {
    @Override
    public CalculatorGame apply(CalculatorGame game) {
        boolean negative = game.getValue() < 0;
        String valString = String.valueOf((int) game.getValue());
        if (negative) {
            valString = valString.substring(1); // shave off minus sign
        }
        // Reverse the string
        valString = new StringBuilder(valString).reverse().toString();
        valString = negative ? "-" + valString : valString; // fix the sign
        return CalculatorGame
            .generateGame(
                valString,
                game.getGoal(),
                game.getMovesLeft() - 1,
                game.getRules(),
                game.getPortals()
            );
    }

    ReverseRule() {
        super(Config.REVERSE);
    }
}
