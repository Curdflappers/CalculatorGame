package rules;

import base.Config;
import base.CalculatorGame;

public class SubtractRule extends Rule {
    public CalculatorGame apply(CalculatorGame game) {
        return CalculatorGame.generateGame(
            game.getValue() - getOperand1(),
            game.getGoal(),
            game.getMovesLeft() - 1,
            game.getRules(),
            game.getPortals()
        );
    }

    public SubtractRule(int operand1) {
        super(Config.SUBTRACT, operand1);
    }
}
