package com.mathwithmark.calculatorgamesolver.calculatorgame;

/**
 * Operates similar to the PadRule, but employs the update method to change the
 * value to be padded.
 */
class StoreRule extends Rule {
    final boolean INITIALIZED;

    @Override
    public CalculatorGame apply(CalculatorGame game) {
        // Do nothing if uninitialized or set to pad negative
        if (!INITIALIZED || getOperand1() < 0) return null;

        // pad the value
        String valString = String.valueOf((int) game.getValue());
        valString += getOperand1();
        return CalculatorGame
            .generateGame(
                valString,
                game.getGoal(),
                game.getMovesLeft() - 1,
                game.getRules(),
                game.getPortals()
            );
    }

    StoreRule() {
        super(Config.STORE);
        INITIALIZED = false;
    }

    StoreRule(int value) {
        super(Config.STORE, value);
        INITIALIZED = true;
    }

    @Override
    public boolean equals(Object other) {
        if (super.equals(other)) {
            StoreRule otherStoreRule = (StoreRule) other;
            return INITIALIZED == otherStoreRule.INITIALIZED;
        }
        return false;
    }
}
