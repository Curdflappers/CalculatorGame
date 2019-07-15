package rules;

import base.Config;

/**
 * Custom rule to modify the state of the Store rule for a given game
 */
class MetaStoreRule extends Rule {
    int storeRuleIndex;

    public CalculatorGame apply(CalculatorGame game) {
        StoreRule storeRule = (StoreRule) game.getRules()[storeRuleIndex];
        if (storeRule == null) return null;

        // Replace this with initialized version and update successor game
        StoreRule newRule = new StoreRule(game.getValue());
        Rule[] newValidRules = game.getRules();
        newValidRules[storeRuleIndex] = newRule;
        return CalculatorGame.generateGame(
            game.getValue(),
            game.getGoal(),
            game.getMovesLeft() - 1,
            newValidRules,
            game.getPortals()
        );
    }

    MetaStoreRule(int storeRuleIndex) {
        super(Config.META_STORE_RULE);
        this.storeRuleIndex = storeRuleIndex;
        // TODO tostring method tells index of store rule if there are multiple
    }
}
