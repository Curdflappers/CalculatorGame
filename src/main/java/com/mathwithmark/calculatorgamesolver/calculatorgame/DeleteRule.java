package com.mathwithmark.calculatorgamesolver.calculatorgame;

class DeleteRule extends Rule {
    @Override
    public CalculatorGame apply(CalculatorGame game) {
        String valString = String.valueOf((int) game.getValue());
        valString = valString.substring(0, valString.length() - 1);
        if (valString.length() == 0 || valString.equals("-")) valString = "0";
        return makeCalculatorGame(
            valString,
            game.getGoal(),
            game.getMovesLeft() - 1,
            game.getRules(),
            game.getPortals()
        );
    }

    DeleteRule() {
        super(Config.DELETE);
    }
}
