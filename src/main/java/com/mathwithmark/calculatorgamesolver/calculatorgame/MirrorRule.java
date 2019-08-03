package com.mathwithmark.calculatorgamesolver.calculatorgame;

class MirrorRule extends Rule {
    @Override
    public CalculatorGame apply(CalculatorGame game) {
        int value = game.getValue();
        boolean negative = value < 0;
        String valString = String.valueOf((int) value);
        if (negative) {
            valString = valString.substring(1); // shave off minus sign
        }
        // add reversed string to end of current string
        valString += new StringBuilder(valString).reverse().toString();
        valString = negative ? "-" + valString : valString;
        return makeCalculatorGame(
            valString,
            game.getGoal(),
            game.getMovesLeft() - 1,
            game.getRules(),
            game.getPortals()
        );
    }

    MirrorRule() {
        super(Config.MIRROR);
    }
}
