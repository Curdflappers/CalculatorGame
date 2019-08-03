package com.mathwithmark.calculatorgamesolver.calculatorgame;

/**
 * Custom rule to modify the state of the Store rule for a given game
 */
class UpdateStoreRule extends Rule {
    /**
     * @return the index of the Store rule of this game. Throws exception if
     * there isn't one
     */
    private static int getStoreRuleIndex(CalculatorGame game) {
        for (int i = 0; i < game.getRules().length; i++) {
            if (game.getRules()[i].getOperator() == Config.STORE) {
                return i;
            }
        }
        throw new IllegalArgumentException(
            "The given game must have a Store rule"
        );
    }

    @Override
    public CalculatorGame apply(CalculatorGame game) {
        // Replace this with initialized version and update successor game
        StoreRule updatedStoreRule = new StoreRule(game.getValue());
        Rule[] newValidRules = game.getRules();
        newValidRules[getStoreRuleIndex(game)] = updatedStoreRule;
        return CalculatorGame
            .generateGame(
                game.getValue(),
                game.getGoal(),
                game.getMovesLeft() - 1,
                newValidRules,
                game.getPortals()
            );
    }

    UpdateStoreRule() {
        super(Config.UPDATE_STORE);
    }
}
