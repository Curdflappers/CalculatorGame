package com.mathwithmark.calculatorgamesolver.calculatorgame;

/**
 * Custom rule to modify the state of the Store rule for a given game
 */
class UpdateStoreRule extends Rule {
    int storeRuleIndex;

    public CalculatorGame apply(CalculatorGame game) {
        StoreRule storeRule = (StoreRule) game.getRules()[storeRuleIndex];
        if (storeRule == null) return null;

        // Replace this with initialized version and update successor game
        StoreRule newRule = new StoreRule(game.getValue());
        Rule[] newValidRules = game.getRules();
        newValidRules[storeRuleIndex] = newRule;
        return CalculatorGame
            .generateGame(
                game.getValue(),
                game.getGoal(),
                game.getMovesLeft() - 1,
                newValidRules,
                game.getPortals()
            );
    }

    UpdateStoreRule(int storeRuleIndex) {
        super(Config.UPDATE_STORE);
        this.storeRuleIndex = storeRuleIndex;
    }
}
