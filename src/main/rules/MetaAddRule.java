package rules;

import base.Config;
import base.CalculatorGame;

/**
 * Adds to the operands of the other rules, does not change the value of the
 * game.
 */
public class MetaAddRule extends Rule {
    public CalculatorGame apply(CalculatorGame game) {
        Rule[] oldRules = game.getValidRules();
        Rule[] newRules = new Rule[oldRules.length];
        int newOperand1, newOperand2;
        for (int i = 0; i < newRules.length; i++) {
            Rule oldRule = oldRules[i];
            newOperand1 = oldRule.getOperand1();
            newOperand2 = oldRule.getOperand2();
            // Changes the operands of all non-"Meta add" rules
            if (oldRule.getOperator() != Config.META_ADD) {
                newOperand1 += getOperand1();
                newOperand2 += getOperand2();
            }
            newRules[i] =
                Rule.makeRule(oldRule.getOperator(), newOperand1, newOperand2);
        }
        return new CalculatorGame(
            game.getValue(),
            game.getGoal(),
            game.getMovesLeft() - 1,
            newRules,
            game.getPortals()
        );
    }

    public MetaAddRule(int operand1) {
        super(Config.META_ADD, operand1);
    }
}
