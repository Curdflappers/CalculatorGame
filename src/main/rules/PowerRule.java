package rules;

import base.Config;
import base.Game;

public class PowerRule extends Rule {
    public Game apply(Game game) {
        return new Game(
            Math.pow(game.getValue(), getOperand1()),
            game.getGoal(),
            game.getMovesLeft() - 1,
            game.getValidRules()
        );
    }

    public PowerRule(int operand1) {
        super(Config.POWER, operand1);
    }
}
