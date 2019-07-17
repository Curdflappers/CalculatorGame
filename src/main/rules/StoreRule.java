package rules;

/**
 * Operates similar to the PadRule, but employs the update method to change the
 * value to be padded.
 */
public class StoreRule extends Rule {
    final boolean INITIALIZED;

    public CalculatorGame apply(CalculatorGame game) {
        // Do nothing if uninitialized or set to pad negative
        if (!INITIALIZED) return game;

        if (getOperand1() < 0) {
            return CalculatorGame
                .generateGame(
                    game.getValue(),
                    game.getGoal(),
                    game.getMovesLeft() - 1,
                    game.getRules(),
                    game.getPortals()
                );
        }

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

    public StoreRule() {
        super(Config.STORE);
        INITIALIZED = false;
    }

    public StoreRule(int value) {
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
