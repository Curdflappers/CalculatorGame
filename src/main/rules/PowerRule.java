package rules;

import base.Config;
import base.CalculatorGame;

public class PowerRule extends Rule {
    public CalculatorGame apply(CalculatorGame game) {
        return new CalculatorGame(
            Math.pow(game.getValue(), getOperand1()),
            game.getGoal(),
            game.getMovesLeft() - 1,
            game.getValidRules(),
            game.getPortals()
        );
    }

    public PowerRule(int operand1) {
        super(Config.POWER, operand1);
    }
}
