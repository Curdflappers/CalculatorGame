package rules;

import base.Config;
import base.CalculatorGame;

public class SignRule extends Rule {
    public CalculatorGame apply(CalculatorGame game) {
        return new CalculatorGame(
            -game.getValue(),
            game.getGoal(),
            game.getMovesLeft() - 1,
            game.getValidRules(),
            game.getPortals()
        );
    }

    public SignRule() {
        super(Config.SIGN);
    }
}
