package rules;

import base.Config;
import base.CalculatorGame;

public class DivideRule extends Rule {
    public CalculatorGame apply(CalculatorGame game) {
        return new CalculatorGame(
            game.getValue() / getOperand1(),
            game.getGoal(),
            game.getMovesLeft() - 1,
            game.getValidRules(),
            game.getPortals()
        );
    }

    public DivideRule(int operand1) {
        super(Config.DIVIDE, operand1);
    }
}
